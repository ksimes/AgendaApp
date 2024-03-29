package com.stronans.android.agenda.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.enums.ViewType;
import com.stronans.android.agenda.fragments.WeekFragment;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Happening;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.controllers.AgendaController;

import java.util.Calendar;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

public class WeekDayView extends View {
    private AgendaConfiguration config;
    private Resources resources;
    private AgendaController controller;
    private DateInfo weekDay;
    private Paint paint;
    private String[] weekNames;
    private List<Happening> todaysEvents;
    private int dayMarker;
    private WeekFragment weekFragment;
    private Context context;

    // Used when inflated from a layout
    public WeekDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.config = AgendaStaticData.getStaticData().getConfig();
        this.resources = context.getResources();
        this.controller = AgendaController.getInst();
        this.context = context;

        weekDay = config.getDateInfo();

        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);

        weekNames = config.getShortWeekDayNames();

        setFocusable(true);

        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // New Selected date will have been set by the touch down action
                // so now we switch to the Day view to show what we have.
                controller.setView(ViewType.Day);
                controller.getTabHost().setCurrentTab(controller.getViewInt());
                return true;
            }
        });
    }

    // Only used when called from another activity
    public WeekDayView(Context context) {
        this(context, null);
    }

    public void setSelectedDate(DateInfo newDate) {
        this.weekDay = DateInfo.fromDateInfo(newDate);
        invalidate();
    }

    public void setDaysEvents(List<Happening> events) {
        this.todaysEvents = events;
        invalidate();
    }

    public void setCurrentDay(int dayMarker) {
        this.dayMarker = dayMarker;
        invalidate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);

        int action = event.getAction();
        // int currentXPosition = (int)event.getX();
        // int currentYPosition = (int)event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                WeekDayView view = weekFragment.getlastDayView();

                if (view != null) {
                    view.invalidate();
                }
                config.setDateInfo(DateInfo.fromDateInfo(weekDay));
                invalidate();
                weekFragment.setlastDayView(this);
                weekFragment.refreshDisplay();
                result = true;
                break;
        }

        return result;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        int dayOfWeek = weekDay.getCurrentDayOfMonth();
        int dayNameSize = getHeight();
        int dateSize = getHeight() / 4;
        int infoSize = getHeight() / 5;
        int dayNameColour;

        Rect DayRect = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawColor(getColor(context, R.color.Ivory));

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        /*
         * Mark the weekend days in a different colour.
         */
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            paint.setColor(getColor(context, R.color.Chalk));
            canvas.drawRect(DayRect, paint);
        }

        RectF selectedMarker = null;
        selectedMarker = new RectF(DayRect);
        selectedMarker.inset(2, 2);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        if (weekDay.isToday()) {
            paint.setColor(getColor(context, R.color.SkyBlue));
            canvas.drawRoundRect(selectedMarker, 10, 10, paint);
        } else if (weekDay.equals(config.getDateInfo())) {
            paint.setColor(getColor(context, R.color.MossGreen));
            canvas.drawRoundRect(selectedMarker, 10, 10, paint);
        }
        // Reset the stroke width
        paint.setStrokeWidth(1);

        /*
         * Lay down the days of the week in large letters on RHS
         */
        String day = weekNames[dayMarker];
        paint.setTextSize(dayNameSize);
        float textWidth = paint.measureText(day);
        // Place the day of the week text at the RHS of the column
        float x = getWidth() - textWidth;

        paint.setStyle(Paint.Style.STROKE);
        dayNameColour = R.color.MediumGrey;
        paint.setColor(getColor(context, dayNameColour));
        canvas.drawText(day, x, dayNameSize - (dayNameSize / 10), paint);

        /*
         * Lay down the date numbers of the days of the week in small numerals on RHS at top
         */
        paint.setTextSize(dateSize);
        int date = weekDay.getDateInMonth();
        String dateString = "" + date + FormattedInfo.suffix(date);
        textWidth = paint.measureText(dateString);
        // Place the date text at the RHS of the column
        x = getWidth() - textWidth - 5;
        float displayWidth = getWidth() - textWidth;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getColor(context, R.color.Black));

        canvas.drawText(dateString, x, dateSize, paint);

        int shift = 3;
        paint.setTextSize(infoSize);
        for (Happening event : todaysEvents) {
            // TODO: Put in icons for the calendar account/event type
            Rect markerRect = new Rect(getPaddingLeft(), shift, getPaddingLeft() + infoSize, shift + infoSize);

            if(event.classType() == Happening.ClassType.Incident) {
                Incident incident = event.getAsIncident();
                paint.setColor(incident.calendarColour());
                canvas.drawRect(markerRect, paint);

                paint.setColor(getColor(context, R.color.Black));
                shift += FormattedInfo.drawTextWrapped(FormattedInfo.getShortEventString(incident),
                        getPaddingLeft() + infoSize + getPaddingLeft(), shift, displayWidth - 10, paint, canvas);
            }
        }

        paint.setColor(getColor(context, R.color.DarkGrey));
        canvas.drawLine(0, 0, getWidth(), 0, paint);
    }

    public void setParent(WeekFragment fragment) {
        weekFragment = fragment;
    }
}
