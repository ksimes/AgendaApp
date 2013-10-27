package com.stronans.android.agenda.views;

import java.text.MessageFormat;
import java.util.List;

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
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.DateInfo;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.controllers.AgendaConfiguration;
import com.stronans.android.controllers.AgendaController;

public class DayView extends View
{
    // Local references to global structures and variables to cut down on accesses.
    AgendaController controller;
    AgendaConfiguration config;
    Resources resources;
    DateInfo selected;
    int titleSize;
    int infoSize;
    Paint paint;
    
    // Used when inflated from a layout
    public DayView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setFocusable(true);
        resources = context.getResources();
        init();
    }

    // Only used when called from another activity
    public DayView(Context context)
    {
        super(context);
        setFocusable(true);
        init();
    }

    private void init()
    {
        config = AgendaStaticData.getStaticData().getConfig();
        controller = AgendaController.getInst();
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
    public void onDraw(Canvas canvas)
    {
        selected = config.getDateInfo();
        canvas.drawColor(resources.getColor(R.color.Ivory));

        int top = 0;
        
        int sizeleft = getHeight() - top;
        titleSize = sizeleft / 25;  
        infoSize = sizeleft / 40;
        
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        
        paint.setColor(resources.getColor(R.color.DarkGrey));
        canvas.drawLine(0, top, getWidth(), top, paint);

        paint.setColor(resources.getColor(R.color.Black));
        DateInfo start = new DateInfo(selected);
        start.setToJustPastMidnight();
        DateInfo end = new DateInfo(selected);
        end.setToMidnight();

        List<Incident> todaysEvents = AgendaData.getInst().getEvents(0, start, end);
        
        top += 5;
        
        if(todaysEvents.isEmpty())
        {
            paint.setTextSize(titleSize);
            top += FormattedInfo.drawWrappedText(resources.getString(R.string.no_activities), 10, top, getWidth() - 20, paint, canvas);
        }
        else
        {
            for(Incident event : todaysEvents)
            {
                paint.setTextSize(titleSize);
                top += FormattedInfo.drawWrappedText(event.getTitle(), 10, top, getWidth() - 20, paint, canvas);
                
                paint.setTextSize(infoSize);
                StringBuffer sb = new StringBuffer(30);
    
                if(!event.isAllDay())
                {
                    sb.append(MessageFormat.format(resources.getString(R.string.time_period), 
                            new Object[] { FormattedInfo.getTimeString(event.getStart()), FormattedInfo.getTimeString(event.getEnd())}));
                }
                else
                    sb.append(resources.getString(R.string.all_day_event));
                
                top += Math.round(Math.abs(paint.ascent()));
                canvas.drawText(sb.toString(), 10, top, paint);
                top += paint.descent();
                if(event.getEventLocation() != null)
                    if(event.getEventLocation().length() > 0)
                    {
                        top += FormattedInfo.drawWrappedText(resources.getString(R.string.location) + event.getEventLocation(), 
                                10, top, getWidth() - 20, paint, canvas);
                    }
    
                if(event.getDescription() != null)
                    if(event.getDescription().length() > 0)
                    {
                        top += FormattedInfo.drawWrappedText(event.getDescription(), 10, top, getWidth() - 20, paint, canvas);
                    }
    
                top += Math.round(Math.abs(paint.ascent()));
            }
        }
    }
}
