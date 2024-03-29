package com.stronans.android.agenda.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.enums.ViewType;
import com.stronans.android.agenda.interfaces.RefreshNotifier;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Happening;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.agenda.support.GridSelection;
import com.stronans.android.controllers.AgendaController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;
import static com.stronans.android.agenda.support.AgendaUtilities.extractForDate;

public class MonthGridView extends View implements RefreshNotifier {
    private static final int INFO_SIZE = 25;

    private AgendaController controller;
    private AgendaConfiguration config;
    private Context context;
    private DateInfo selected;
    private GridSelection gridData;
    private List<Happening> eventList = null; // List of all events which occur in this grid (may include extra days before
    // beginning and after end of month).
    private Refresher refresher;

    // Used when being inflated from a layout
    public MonthGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        this.context = context;
        init();
    }

    // Only used when called from another activity
    public MonthGridView(Context context) {
        super(context);
        setFocusable(true);
        init();
    }

    private void init() {
        config = AgendaStaticData.getStaticData().getConfig();
        controller = AgendaController.getInst();

        gridData = new GridSelection();
        gridData.setNumColumns(7);

        setOnTouchListener(new touchOnScreen());

        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // New Selected date will have been set by the touch down action
                // so now we switch to the week view to show what we have.
                controller.setView(ViewType.Week);
                controller.getTabHost().setCurrentTab(controller.getViewInt());
                return false;
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.stronans.android.agenda.interfaces.RefreshNotifier#addRefreshNotifier(com.stronans.android.agenda.interfaces.Refresher
     * )
     */
    @Override
    public void addRefreshNotifier(Refresher refresher) {
        this.refresher = refresher;
    }

    private class touchOnScreen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getAction();
            int currentXPosition = (int) event.getX();
            int currentYPosition = (int) event.getY();

            DateInfo date = gridData.dateHit(currentXPosition, currentYPosition);

            if (action == MotionEvent.ACTION_DOWN) {
                // If this is a touch and it was in the grid and the month is correct
                // update the selected date and the agenda at the bottom of the screen.
                if (date != null) {
                    selected = date;
                    config.setDateInfo(DateInfo.fromDateInfo(selected));
                    refresher.refreshDisplay();
                } else if (currentYPosition > gridData.getBottom()) {
                    // TODO: Display list sub-dialog of all events for this day.
                }
            }

            // if (action == MotionEvent.ACTION_MOVE)
            // {
            // // TODO: start the swipe up/down to move back/forward a month (move to same selected day in that month)
            // }
            return false;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // canvas.drawColor(private  R.color.Chalk));
        canvas.drawColor(Color.WHITE);
        selected = config.getDateInfo();

        int top = 0;
        // Draw days of the week text & weekend bands as beige & grid
        // Draw previous months days & after months days as grey
        // Draw month dates
        // Draw tokens on day any all day marker then up to two other markers and a continuation marker if required
        top = drawMonthInformation(canvas, top);

        top = drawEventText(canvas, top);
    }

    private int drawMonthInformation(Canvas canvas, int top) {
        int monthTextSize;
        int weekTextSize;

        DateInfo startOfMonthGrid, endOfMonthGrid;

        Paint paint = new Paint();
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        gridData.setCellWidth(canvas.getWidth() / 7); // seven segments for the days of the week
        gridData.setCellHeight(gridData.getCellWidth()); // Make the grid square
        monthTextSize = gridData.getCellWidth() / 3; // 1/3 of the square grid
        weekTextSize = gridData.getCellWidth() / 4; // 1/4 of the square grid

        paint.setTextSize(weekTextSize);
        int weekstart = config.getWeekStart(); // User defined start day of the week, i.e. Sunday, Saturday, Monday, etc.
        String[] weekDaysShort = config.getShortWeekDayNames();

        top += Math.round(Math.abs(paint.ascent()) + paint.descent());
        gridData.setTop(top);

        for (int i = 0; i < 7; i++, weekstart++) {
            if (weekstart == 8)
                weekstart = 1;

            String day = weekDaysShort[weekstart - 1];
            float textWidth = paint.measureText(day);

            // Centre the day of the week text in the day column
            float x = (i * gridData.getCellWidth()) + (gridData.getCellWidth() / 2 - (textWidth / 2));

            canvas.drawText(day, x, top - 5, paint);

            // Now draw the weekend markers in a slightly different colour
            if ((weekstart == Calendar.SUNDAY) || (weekstart == Calendar.SATURDAY)) {
                Paint wkpaint = new Paint(paint);
                Rect wkRect = new Rect((int) (i * gridData.getCellWidth()), top,
                        (int) ((i * gridData.getCellWidth()) + gridData.getCellWidth()), top
                        + (int) (gridData.getCellWidth() * 6));

                wkpaint.setColor(getColor(context, R.color.Chalk));
                canvas.drawRect(wkRect, wkpaint);
            }
        }

        // Draw the month grid
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);

        for (int i = 0; i < 8; i++) {
            canvas.drawLine((i * gridData.getCellWidth()), top, (i * gridData.getCellWidth()), top + gridData.getCellWidth()
                    * 6, paint);
        }

        // Six weeks will cover any month in the year
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(0, top + gridData.getCellHeight() * i, gridData.getCellWidth() * 7, top
                    + (gridData.getCellHeight() * i), paint);
        }

        // Draw out all the event markers for the month

        weekstart = config.getWeekStart(); // User defined start day of the week, i.e. Sunday, Monday, Tue... etc.
        int firstDay = selected.getFirstDayOfMonth();
        int lastDay = selected.getDaysInMonth();
        boolean outsideMonth = true;

        paint.setTextSize(monthTextSize);

        startOfMonthGrid = DateInfo.fromDate(1, selected.getMonth(), selected.getYear());

        int calc = ((firstDay + 7 - weekstart) % 7);
        startOfMonthGrid.addToDate(-calc);

        gridData.dateOnStartOfGrid = DateInfo.fromDateInfo(startOfMonthGrid);
        gridData.dateOnStartOfGrid.setToJustPastMidnight();

        endOfMonthGrid = DateInfo.fromDateInfo(startOfMonthGrid);
        endOfMonthGrid.addToDate(42);
        endOfMonthGrid.setToMidnight();

        eventList = AgendaData.getInst().getEvents(0, startOfMonthGrid, endOfMonthGrid);

        // Six weeks will cover any month
        for (int weeks = 0; weeks < 6; weeks++) {
            for (int daysOfWeek = 0; daysOfWeek < 7; daysOfWeek++, weekstart++) {
                paint.setStrokeWidth(0);
                if (weekstart == 8)
                    weekstart = 1;

                Rect cellRect = gridData.getCellRect(daysOfWeek, weeks);

                if (outsideMonth && (firstDay == weekstart))
                    outsideMonth = false;

                int dayOfMonth = startOfMonthGrid.getDateInMonth();
                String date = "" + dayOfMonth;

                if (outsideMonth) {
                    Rect background = new Rect(cellRect);
                    background.inset(1, 1);
                    Paint backPaint = new Paint(paint);
                    backPaint.setColor(getColor(context, R.color.LightGrey));
                    backPaint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(background, backPaint);

                    backPaint.setColor(getColor(context, R.color.DarkGrey));
                    Rect mRect = new Rect();
                    paint.getTextBounds(date, 0, date.length(), mRect);
                    canvas.drawText(date, background.left + 1, background.top + mRect.height() + 1, backPaint);
                } else {
                    RectF selectedMarker = null;
                    selectedMarker = gridData.getCellRectF(daysOfWeek, weeks);
                    selectedMarker.inset(2, 2);

                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(6);
                    if (startOfMonthGrid.isToday()) {
                        paint.setColor(getColor(context, R.color.SkyBlue));
                        canvas.drawRoundRect(selectedMarker, 10, 10, paint);
                    } else if (startOfMonthGrid.equals(selected)) {
                        paint.setColor(getColor(context, R.color.MossGreen));
                        canvas.drawRoundRect(selectedMarker, 10, 10, paint);
                    }

                    //
                    paint.setStyle(Paint.Style.FILL);
                    paint.setStrokeWidth(1);
                    paint.setColor(getColor(context, R.color.Black));

                    Rect mRect = new Rect();
                    paint.getTextBounds(date, 0, date.length(), mRect);
                    canvas.drawText(date, cellRect.left + 2, cellRect.top + mRect.height() + 2, paint);

                    IconInfo events = getTokens(eventList, startOfMonthGrid);

                    // All day category marker
                    if (events.allDay != null)
                        canvas.drawBitmap(events.allDay, cellRect.left + gridData.getCellWidth() / 2 + 2, cellRect.top + 2,
                                paint);

                    int startLeft = cellRect.left + 2;
                    int startTop = cellRect.top + gridData.getCellHeight() / 2;

                    for (Bitmap image : events.others) {
                        canvas.drawBitmap(image, startLeft, startTop, paint);
                        startLeft += gridData.getCellWidth() / 2 + 2;
                    }
                }

                if (!outsideMonth && (dayOfMonth == lastDay)) {
                    outsideMonth = true;
                    weekstart = 9;
                }

                startOfMonthGrid.addToDate(1);
            }
        }

        gridData.setBottom(top + gridData.getCellHeight() * 6);
        return gridData.getBottom();
    }

    private class IconInfo {
        private Bitmap allDay = null;
        private List<Bitmap> others = null;

        private IconInfo() {
            others = new ArrayList<Bitmap>();
        }
    }

    private IconInfo getTokens(List<Happening> eventList, DateInfo selected) {
        IconInfo images = new IconInfo();

        List<Happening> todaysEvents = extractForDate(eventList, selected);

        for (Happening event : todaysEvents) {
            if(event.classType() == Happening.ClassType.Incident ) {
                Incident incident = event.getAsIncident();
                if (incident.isAllDay())
                    images.allDay = this.config.getScaledToken(incident.category().marker(),
                            gridData.getCellWidth() / 2 - 2, gridData.getCellWidth() / 2 - 2);
                else {
                    images.others.add(this.config.getScaledToken(incident.category().marker(),
                            gridData.getCellWidth() / 2 - 2, gridData.getCellWidth() / 2 - 2));
                    // images.others.add(this.config.getToken(event.getCategory().getMarker()));
                }
            }
        }

        return images;
    }

    private int drawEventText(Canvas canvas, int top) {
        Paint paint = new Paint();
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(INFO_SIZE);

        List<Happening> todaysEvents = extractForDate(eventList, selected); // Assumed that eventList set in last
        // routine.
        top += 5;

        for (Happening event : todaysEvents) {
            top += Math.round(Math.abs(paint.ascent()));
            canvas.drawText(FormattedInfo.getShortEventString(event.getAsIncident()), 10, top, paint);
            top += paint.descent();
        }

        return top;
    }
}
