package com.stronans.android.agenda.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.model.Happening;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.controllers.AgendaController;

import java.text.MessageFormat;
import java.util.List;

import static android.support.v4.content.ContextCompat.getColor;

public class DayView extends View {
    // Local references to global structures and variables to cut down on accesses.
    private AgendaController controller;
    private AgendaConfiguration config;
    private Resources resources;
    private DateInfo selected;
    private int titleSize;
    private int infoSize;
    private Paint paint;
    private Context context;

    // Used when inflated from a layout
    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        resources = context.getResources();
        init(context);
    }

    // Only used when called from another activity
    public DayView(Context context) {
        super(context);
        setFocusable(true);
        init(context);
    }

    private void init(Context context) {
        config = AgendaStaticData.getStaticData().getConfig();
        controller = AgendaController.getInst();
        this.context = context;
        paint = new Paint();

//        setOnTouchListener(new touchOnScreen());

//        setLongClickable(true);
//        setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v)
//            {
//                // New Selected date will have been set by the touch down action 
//                // so now we switch to the week view to show what we have.
//                controller.setView(ViewType.Week);
//                controller.getTabHost().setCurrentTab(controller.getViewInt());
//                return false;
//            }
//        });
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        selected = config.getDateInfo();
        canvas.drawColor(getColor(context, R.color.Ivory));

        int top = 0;

        int sizeleft = getHeight() - top;
        titleSize = sizeleft / 25;
        infoSize = sizeleft / 40;

        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(getColor(context, R.color.DarkGrey));
        canvas.drawLine(0, top, getWidth(), top, paint);

        paint.setColor(getColor(context, R.color.Black));
        DateInfo start = DateInfo.fromDateInfo(selected);
        start.setToJustPastMidnight();
        DateInfo end = DateInfo.fromDateInfo(selected);
        end.setToMidnight();

        List<Happening> todaysEvents = AgendaData.getInst().getEvents(0, start, end);

        top += 5;

        if (todaysEvents.isEmpty()) {
            paint.setTextSize(titleSize);
            top += FormattedInfo.drawTextWrapped(resources.getString(R.string.no_activities), 10, top, getWidth() - 20, paint, canvas);
        } else {
            for (Happening event : todaysEvents) {
                paint.setTextSize(titleSize);
                top += FormattedInfo.drawTextWrapped(event.title(), 10, top, getWidth() - 20, paint, canvas);

                paint.setTextSize(infoSize);
                StringBuilder sb = new StringBuilder(30);

                Incident incident = event.getAsIncident();

                if (!incident.isAllDay()) {
                    sb.append(MessageFormat.format(resources.getString(R.string.time_period),
                            new Object[]{DateInfo.getTimeString(incident.startAt()), DateInfo.getTimeString(incident.endsAt())}));
                } else
                    sb.append(resources.getString(R.string.all_day_event));

                top += Math.round(Math.abs(paint.ascent()));
                canvas.drawText(sb.toString(), 10, top, paint);
                top += paint.descent();
                if (incident.eventLocation() != null)
                    if (incident.eventLocation().length() > 0) {
                        top += FormattedInfo.drawTextWrapped(resources.getString(R.string.location) + incident.eventLocation(),
                                10, top, getWidth() - 20, paint, canvas);
                    }

                if (event.description() != null)
                    if (event.description().length() > 0) {
                        top += FormattedInfo.drawTextWrapped(event.description(), 10, top, getWidth() - 20, paint, canvas);
                    }

                top += Math.round(Math.abs(paint.ascent()));
            }
        }
    }
}
