package com.stronans.android.agenda.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.enums.LabelOrientation;
import com.stronans.android.agenda.enums.ViewType;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Happening;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.controllers.AgendaController;

import java.util.Calendar;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;
import static com.stronans.android.agenda.support.AgendaUtilities.extractForDate;

public class YearView extends View {
    private static final int MONTHNAME_SIZE = 30;
    private static final int TITLE_SIZE = 40;  // 25;

    private AgendaConfiguration config;
    private DateInfo selected;
    private String[] monthNames;
    private AgendaController controller;
    private List<Happening> eventList = null; // List of all events which occur in this grid (may include extra days before
    // beginning and after end of month).
    private int cellBackground, singleEvent, doubleEvent, moreEvents, weekend;
    private Context context;

    // Used when inflated from a layout
    public YearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        init(context);
    }

    // Only used when called from another activity
    public YearView(Context context) {
        super(context);
        setFocusable(true);
        init(context);
    }

    private void init(Context context) {
        config = AgendaStaticData.getStaticData().getConfig();
        controller = AgendaController.getInst();
        this.context = context;

        selected = config.getDateInfo();
        monthNames = config.getLongMonthNames();

        // setOnTouchListener(new touchOnScreen()); // TODO

        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // New Selected month (set to the 1st) will have been set by the touch down action
                // so now we switch to the Month view to show what we have.
                controller.setView(ViewType.Month);
                controller.getTabHost().setCurrentTab(controller.getViewInt());
                return false;
            }
        });

        setColours();
    }

    private void setColours() {
        cellBackground = getColor(context, R.color.Ivory);
        singleEvent = getColor(context, R.color.ForestGreen);
        doubleEvent = getColor(context, R.color.Amber);
        moreEvents = getColor(context, R.color.red);
        weekend = getColor(context, R.color.red); // BrickRed
    }

    private void refreshEvents() {
        DateInfo startOfYearGrid, endOfYearGrid;

        startOfYearGrid = DateInfo.fromDate(1, Calendar.JANUARY, selected.getYear());

        endOfYearGrid = DateInfo.fromDate(31, Calendar.DECEMBER, selected.getYear());
        endOfYearGrid.setToMidnight();

        eventList = AgendaData.getInst().getEvents(0, startOfYearGrid, endOfYearGrid);
    }

    private int getbackgroundColour(List<Happening> eventList, DateInfo selected) {
        int backColour = cellBackground;
        switch (extractForDate(eventList, selected).size()) {
            case 0:
                backColour = cellBackground;
                break;

            case 1:
                backColour = singleEvent;
                break;

            case 2:
                backColour = doubleEvent;
                break;

            default:
                backColour = moreEvents;
                break;
        }

        return backColour;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(cellBackground);
        refreshEvents();
        int top = 0;
        // Draw Year header text (Which is the year plus buttons to roll up or down)
        top = drawHeader(canvas, top);
        top = drawMonthGrid(canvas, top);
    }

    private int drawHeader(Canvas canvas, int Y) {
        GradientDrawable mDrawable;
        int headerHeight;
        Rect mRect;
        int width = canvas.getWidth();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // First calculate how high the header should be
        paint.setTextSize(TITLE_SIZE);
        headerHeight = Math.round(Math.abs(paint.ascent()) + paint.descent());

        // Get the colours for the graduated header box
        int[] colourRange = AgendaStaticData.getStaticData().monthColourRange(selected);

        // and draw it out
        mDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colourRange);
        mDrawable.setShape(GradientDrawable.RECTANGLE);
        mDrawable.setGradientRadius((float) (Math.sqrt(2) * 60));

        mRect = new Rect(0, Y, width, Y + headerHeight);

        mDrawable.setBounds(mRect);
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.draw(canvas);

        String dateString = DateInfo.getYearString(selected);

        return Y + FormattedInfo.drawTextInBox(dateString, mRect, TITLE_SIZE, config.getHeaderOrientation(), paint, canvas);
    }

    private int drawMonthGrid(Canvas canvas, int Y) {
        int top = Y;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);

        float cellWidth = getWidth() / 3;
        float cellHeight = (getHeight() - Y) / 4;

        // left edge and top line
        canvas.drawLine(0, top, 0, top + cellHeight * 4, paint);
        canvas.drawLine(0, top, cellWidth * 3, top, paint);

        DateInfo month = DateInfo.fromDateInfo(selected);
        month.setMonth(Calendar.JANUARY);

        for (int i = 0; i < 4; i++) {
            int shiftY = (int) (top + (cellHeight * i));
            canvas.drawLine(0, shiftY, cellWidth * 3, shiftY, paint);

            for (int j = 0; j < 3; j++) {
                int shiftX = (int) (j * cellWidth);
                canvas.drawLine(shiftX, top, shiftX, top + (cellHeight * 4), paint);

                drawMonth(canvas, month, shiftX, shiftY, cellWidth, cellHeight);

                month.rollMonthForward();
            }
        }

        return top;
    }

    public int getScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    private void drawMonth(Canvas canvas, DateInfo month, int x, int y, float width, float height) {
        Rect mRect, bounds;
        int shift = y;
        String monthName;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(MONTHNAME_SIZE);
        paint.setFakeBoldText(true);

        float cellWidth = width / 7; // seven segments for the days of the week

        monthName = monthNames[month.getMonth()];

        mRect = new Rect();
        paint.getTextBounds(monthName, 0, monthName.length(), mRect);
        int step = Math.round(Math.abs(paint.ascent()) + paint.descent());
        bounds = new Rect(x, y, x + (int) width, y + step);
        shift += FormattedInfo.drawTextInBox(monthName, bounds, MONTHNAME_SIZE, LabelOrientation.centre, paint, canvas);

        int weekstart = config.getWeekStart(); // Actual start day of the week, i.e. Sunday, Monday, Tue... etc.
        int firstDay = month.getFirstDayOfMonth();
        int lastDay = month.getDaysInMonth();
        boolean beforeStart = true;
        boolean afterEnd = false;

        paint.setFakeBoldText(false);
        paint.setTypeface(Typeface.MONOSPACE);

        float cellHeight = (height - step) / 6; // Six weeks will cover any month

        switch (getScreenOrientation()) {
            case Configuration.ORIENTATION_PORTRAIT:
                paint.setTextSize((float) (cellHeight * 0.7)); // 75% of height of cell
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                paint.setTextSize(cellHeight); // 100% of height of cell
                break;
        }

        int dayofMonth = 0;
        // Six weeks will cover any month
        for (int weeks = 0; weeks < 6; weeks++) {
            for (int daysOfWeek = 0; daysOfWeek < 7; daysOfWeek++, weekstart++) {
                if (weekstart == 8)
                    weekstart = 1;

                Rect cellRect = new Rect(x + (int) (daysOfWeek * cellWidth) + 2,
                        shift + (int) (cellHeight * weeks) + 2,
                        x + (int) ((daysOfWeek * cellWidth) + cellWidth) - 2,
                        shift + (int) (cellHeight) + (int) (cellHeight * weeks) - 2);

                if (beforeStart && (firstDay == weekstart))
                    beforeStart = false;

                if (!afterEnd && (dayofMonth >= lastDay))
                    afterEnd = true;

                if (beforeStart || afterEnd) {
                    paint.setColor(cellBackground);
                    canvas.drawRect(cellRect, paint);
                } else {
                    dayofMonth++;

                    DateInfo di = DateInfo.fromDate(dayofMonth, month.getMonth(), month.getYear());

                    String date = String.valueOf(dayofMonth);
                    mRect = new Rect(0, 0, 0, 0);
                    paint.getTextBounds(date, 0, date.length(), mRect);

                    paint.setColor(getbackgroundColour(eventList, di));
                    canvas.drawRect(cellRect, paint);

                    if (paint.getColor() != cellBackground) {
                        paint.setColor(Color.WHITE);
                    } else {
                        if ((weekstart == Calendar.SUNDAY) || (weekstart == Calendar.SATURDAY))
                            paint.setColor(weekend);
                        else
                            paint.setColor(Color.BLACK);
                    }

                    canvas.drawText(date, (cellRect.left + cellRect.width()) - mRect.width() - 6, cellRect.top + mRect.height()
                            + 5, paint);

                    if (di.isToday()) {
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(4);
                        RectF selectedMarker = new RectF(cellRect);
                        paint.setColor(Color.BLUE);
                        selectedMarker.inset(-3, -3);
                        canvas.drawRoundRect(selectedMarker, 5, 5, paint);
                        paint.setColor(Color.BLACK);
                        paint.setStrokeWidth(1);
                        paint.setStyle(Paint.Style.FILL);
                    }
                }
            }
        }
    }
}
