package com.stronans.android.agenda.views;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.stronans.android.agenda.R;
import com.stronans.android.agenda.dataaccess.AgendaData;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.enums.ViewType;
import com.stronans.android.agenda.model.Incident;
import com.stronans.android.agenda.support.DateInfo;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.controllers.AgendaConfiguration;
import com.stronans.android.controllers.AgendaController;

public class YearView extends View
{
    static final int MONTHNAME_SIZE = 30;
    static final int TITLE_SIZE = 40;   // 25;

    AgendaConfiguration config;
    Resources resources;
    DateInfo selected;
    String[] monthNames;
    AgendaController controller;
    List<Incident> eventList = null;          // List of all events which occur in this grid (may include extra days before beginning and after end of month).

    // Used when inflated from a layout
    public YearView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setFocusable(true);
        resources = context.getResources();
        init();
   }

    // Only used when called from another activity
    public YearView(Context context)
    {
        super(context);
        setFocusable(true);
        init();
    }

    private void init()
    {
        config = AgendaStaticData.getStaticData().getConfig();
        controller = AgendaController.getInst();

        selected = config.getDateInfo();
        monthNames = config.getLongMonthNames();
        
//        setOnTouchListener(new touchOnScreen()); // TODO
        
        setLongClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                // New Selected month (set to the 1st) will have been set by the touch down action 
                // so now we switch to the Month view to show what we have.
                controller.setView(ViewType.Month);
                controller.getTabHost().setCurrentTab(controller.getViewInt());
                return false;
            }
        });
    }
    
    private void refreshEvents()
    {
        DateInfo startOfYearGrid, endOfYearGrid;

        startOfYearGrid = new DateInfo();
        startOfYearGrid.getCalendar().set(
                selected.getCalendar().get(Calendar.YEAR), 
                Calendar.JANUARY, 1);

        endOfYearGrid = new DateInfo();
        endOfYearGrid.getCalendar().set(
                selected.getCalendar().get(Calendar.YEAR), 
                Calendar.DECEMBER, 31);
        endOfYearGrid.setToMidnight();
        
        eventList = AgendaData.getInst().getEvents(0, startOfYearGrid, endOfYearGrid);
    }
    
    private int getbackgroundColour(List<Incident> eventList, DateInfo selected)
    {
        int backColour = resources.getColor(R.color.Ivory);
        switch(Incident.extractForDate(eventList, selected).size())
        {
        case 0 : 
            backColour = resources.getColor(R.color.Ivory);
            break;

        case 1 : 
            backColour = resources.getColor(R.color.ForestGreen);
            break;

        case 2 : 
            backColour = resources.getColor(R.color.DarkOrange);
            break;
        
        default:
            backColour = resources.getColor(R.color.red);
            break;
        }
        
        return backColour;
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawColor(resources.getColor(R.color.Ivory));
        refreshEvents();
        int top = 0;
        // Draw Year header text (Which is the year plus buttons to roll up or down)
        top = drawHeader(canvas, top);
        top = drawMonthGrid(canvas, top);
    }
    
    private int drawHeader(Canvas canvas, int Y)
    {
        int top = Y; 
        GradientDrawable mDrawable;
        int shift = top;
        int headerHeight = 0;
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
        mDrawable.setGradientRadius((float)(Math.sqrt(2) * 60));
  
        mRect = new Rect(0, top, width, top + headerHeight);

        mDrawable.setBounds(mRect);
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.draw(canvas);
        
        String dateString = FormattedInfo.getYearString(selected);
        paint.getTextBounds(dateString, 0, dateString.length(), mRect); 
        int x = 0;
        switch(config.getHeaderOrientation())
        {
        case left:
            x = 5;
            break;
        case right:
            x = width - mRect.width() - 5;
            break;
        case centre:
            x = (width / 2) - (mRect.width() / 2);
            break;
        }
        paint.setTextSize(TITLE_SIZE);
        paint.getTextBounds(dateString, 0, dateString.length(), mRect);
        shift += mRect.height();
        canvas.drawText(dateString, x, shift + paint.descent(), paint);
        
        return top + headerHeight;
    }
    
    private int drawMonthGrid(Canvas canvas, int Y)
    {
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

        DateInfo month = new DateInfo(selected.getDate());
        month.setMonth(Calendar.JANUARY);
        
        for(int i = 0; i < 4; i++)
        {
            int shiftY = (int)(top + (cellHeight * i));
            canvas.drawLine(0, shiftY, cellWidth * 3, shiftY, paint);

            for(int j = 0; j < 3; j++)
            {
                int shiftX = (int)(j * cellWidth);
                canvas.drawLine(shiftX, top, shiftX, top + (cellHeight * 4), paint);
                
                drawMonth(canvas, month, shiftX, shiftY, cellWidth, cellHeight);
                
                month.rollMonthForward();
            }
        }
        
        return top;
    }
    
    public int getScreenOrientation()
    {
        return getResources().getConfiguration().orientation;
    }
    
    private void drawMonth(Canvas canvas, DateInfo month, int x, int y, float width, float height)
    {
        Rect mRect;
        int shift = y;
        String monthName;        
        
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(MONTHNAME_SIZE);
        paint.setFakeBoldText(true);

        float cellWidth = width / 7;    // seven segments for the days of the week
        
        monthName = monthNames[month.getCalendar().get(Calendar.MONTH)];

        mRect = new Rect();
        paint.getTextBounds(monthName, 0, monthName.length(), mRect);
        int step = Math.round(Math.abs(paint.ascent()) + paint.descent()); 
        shift += step;
        canvas.drawText(monthName, x + (width / 2) - (mRect.width() / 2), shift - paint.descent(), paint);
        shift += 2;

        int weekstart = config.getWeekStart();      // Actual start day of the week, i.e. Sunday, Monday, Tue... etc.
        int firstDay = month.getFirstDayOfMonth(); 
        int lastDay = month.getDaysInMonth(); 
        boolean beforeStart = true;
        boolean afterEnd = false;
        
        paint.setFakeBoldText(false);
        paint.setTypeface(Typeface.MONOSPACE);

        float cellHeight = (height - step)/ 6;  // Six weeks will cover any month
        
        switch(getScreenOrientation())
        {
            case Configuration.ORIENTATION_PORTRAIT:
                paint.setTextSize((float)(cellHeight * 0.7));      // 75% of height of cell   
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                paint.setTextSize(cellHeight);      // 100% of height of cell   
                break;
        }
        
        int dayofMonth = 0;
        // Six weeks will cover any month
        for(int weeks = 0; weeks < 6; weeks++)
        {
            for(int daysOfWeek = 0; daysOfWeek < 7; daysOfWeek++, weekstart++)
            {
                if(weekstart == 8)
                    weekstart = 1;
                
                Rect cellRect = new Rect(x + (int)(daysOfWeek * cellWidth) + 2, 
                                shift + (int)(cellHeight * weeks) + 2, 
                                x + (int)((daysOfWeek * cellWidth) + cellWidth) - 2, 
                                shift + (int)(cellHeight) + (int)(cellHeight * weeks) - 2);

                if(beforeStart && (firstDay == weekstart))
                    beforeStart = false;
                
                if(!afterEnd && (dayofMonth >= lastDay))
                    afterEnd = true;

                if(beforeStart || afterEnd)
                {
                    paint.setColor(resources.getColor(R.color.Ivory));
                    canvas.drawRect(cellRect, paint);
                }
                else
                {
                    dayofMonth++;
                    Calendar info = month.getCalendar();
                    
                    DateInfo di = new DateInfo(new GregorianCalendar(info.get(Calendar.YEAR), 
                            info.get(Calendar.MONTH), dayofMonth, 0, 0, 0).getTime());

                    String date = String.valueOf(dayofMonth);
                    mRect = new Rect(0, 0, 0, 0);
                    paint.getTextBounds(date, 0, date.length(), mRect);

                    paint.setColor(getbackgroundColour(eventList, di));
                    canvas.drawRect(cellRect, paint);

                    if(paint.getColor() != resources.getColor(R.color.Ivory))
                    {
                        paint.setColor(resources.getColor(R.color.White));
                    }
                    else
                    {
                        if((weekstart == Calendar.SUNDAY) ||(weekstart == Calendar.SATURDAY))
                            paint.setColor(resources.getColor(R.color.RegalRed));
                        else
                            paint.setColor(resources.getColor(R.color.Black));
                    }
                    
                    canvas.drawText(date, (cellRect.left + cellRect.width()) -  mRect.width() - 6, cellRect.top + mRect.height() + 5, paint);

                    if(di.isToday())
                    {
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(4);
                        RectF selectedMarker = new RectF(cellRect);
                        paint.setColor(resources.getColor(R.color.ElectricBlue));
                        selectedMarker.inset(-3, -3);
                        canvas.drawRoundRect(selectedMarker, 5, 5, paint);
                        paint.setColor(resources.getColor(R.color.Black));
                        paint.setStrokeWidth(1);
                        paint.setStyle(Paint.Style.FILL);
                    }
                }
            }
        }
    }
}
