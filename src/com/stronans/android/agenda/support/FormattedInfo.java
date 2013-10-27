package com.stronans.android.agenda.support;

import java.text.DateFormat;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.stronans.android.agenda.enums.LabelOrientation;
import com.stronans.android.agenda.model.Incident;

public class FormattedInfo
{
    public static final int DEFAULT_LEFT_MARGIN = 5; 
    public static final int DEFAULT_RIGHT_MARGIN = 5;
    
    static private String formatInfo(DateInfo timeInfo, String format)
    {
        DateFormat output = DateFormat.getDateInstance(DateFormat.DEFAULT); 
        return output.format(timeInfo.getDate());
    }
    
    static public String getUniversalString(DateInfo timeInfo)
    {
        return formatInfo(timeInfo, "yyyyMMddHHmmss");
    }
    
    static public String getTimeString(DateInfo timeInfo)
    {
        return formatInfo(timeInfo, "HH:mm");
    }
    
    static public String getDateString(DateInfo timeInfo)
    {
        return formatInfo(timeInfo, "EEEE dd MMMM");
    }
    
    static public String getDateTimeString(DateInfo timeInfo)
    {
        return formatInfo(timeInfo, "HH:mm:ss dd/MM/yyyy");
    }

    static public String getYearString(DateInfo timeInfo)
    {
        return formatInfo(timeInfo, "yyyy");
    }
    
    static public String getMonthString(DateInfo timeInfo)
    {
        return formatInfo(timeInfo, "MMMM");
    }

    static public String getShortWeekdayString(DateInfo timeInfo)
    {
        return formatInfo(timeInfo, "EEE");
    }
    
    /*
     * Given the day of the month add a suffix to give date strings
     */
    static public String suffix(int value)
    {
        String result = "";
        String strValue = String.valueOf(value);
        Character last = strValue.charAt(strValue.length() - 1);
        
        switch(last)
        {
        case '1':
            result = "st"; 
            break;
            
        case '2':
            result = "nd"; 
            break;
               
        case '3':
            result = "rd"; 
            break;
               
        default:
            result = "th"; 
            break;
        }
        
        return result;
    }
    
    static public String getShortEventString(Incident event)
    {
        StringBuffer sb = new StringBuffer(30);
        sb.append(event.title());

        if(!event.isAllDay())
        {
            sb.append("  [");
            sb.append(FormattedInfo.getTimeString(event.startAt()));
            sb.append(" - ");
            sb.append(FormattedInfo.getTimeString(event.endsAt()));
            sb.append("]");
        }
        else
            sb.append("  (All day)");
        
        return sb.toString();
    }
    
    /**
     * Takes a string and returns a string with carriage returns added at the spaces where the text needs to break to wordwrap to fit in the give size.
     * @param text Text that is to be wordwrapped
     * @param paint Graphics Context to use for string sizing.
     * @param width Width that the string is to fit into 
     * @return string with carriage returns added at the points where the text needs to wordwrap to fit in the give size.
     */
    static String computeDrawString(String text, Paint paint, float width)
    {
        String drawString = new String(text);      // Copy the original string

        float extent = paint.measureText(drawString);
        if(extent > width)
        {
            String output = "";

            boolean finished = false;
            int lastPoint = drawString.length();
            String part = drawString;
            int startpoint = 0;
            int i = 0;
            
            while(!finished)
            {
                if(paint.measureText(part) < width)
                    break;

                while(paint.measureText(part) > width)
                {
                    i = drawString.lastIndexOf(' ', lastPoint);
                    if(i == -1)
                        break;
                    
                    part = drawString.substring(startpoint, i);
                    lastPoint = i - 1;
                }
                
                if(i == -1)
                    finished = true;
                
                startpoint = lastPoint + 2; 
                output += part;
                output += '\r';
                part = drawString.substring(startpoint, drawString.length());
                lastPoint = drawString.length();
            }
            
            output += part;
            
            drawString = output; 
        }
        
        return drawString;
    }
    
    /**
     * Takes a string, wordwrap it to fit in the give size (see above routine) and then draws it on the canvas.
     * @param text Text that is to be wordwrapped
     * @param x X-coordinate that the text is to start at on the canvas.   
     * @param y Y-coordinate that the text is to start at on the canvas.
     * @param width Width that the string is to fit into 
     * @param paint Graphics Context to use for string sizing.
     * @return int size of the height of text which has been drawn.
     */
    static public int drawTextWrapped(String text, int x, int y, float width, Paint paint, Canvas canvas)
    {
        int height = 0;
        
        // First prepare the wordwrap (see above).
        String formattedString = computeDrawString(text, paint, width);
        
        // Split it into an array by the carriage returned inserted by above routine.
        String[] displayArray = new String(formattedString).split("\r");
        
        // and draw it out on the screen.
        for(String drawString : displayArray)
        {
            height += Math.round(Math.abs(paint.ascent()));
            canvas.drawText(drawString, x, y + height, paint);
            height += paint.descent();
        }
        
        // return how much vertical space has been used on the display 
        return height;
    }
    /**
     * take a string and positions it vertically aligned in the box dimension provided. 
     * The horizontal positioning depends on the orientation parameter passed in.
     * @param text Text that is to be position in the box dimensions
     * @param box dimension provided to wrap round the text
     * @param textSize the size of the text applied to the paint object
     * @param paint Graphics Context to use for string sizing
     * @param canvas canvas the text is to be painted onto
     * @return the height of the box
     */
    static public int drawTextInBox(String text, Rect box, int textSize, LabelOrientation orientation, Paint paint, Canvas canvas)
    {
        Rect textBounds = new Rect();
        int width = box.width();
       
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), textBounds); 

        int x = box.left;
        switch(orientation)
        {
        case left:
            x += DEFAULT_LEFT_MARGIN;  // default Left margin
            break;
        case right:
            x += width - textBounds.width() - DEFAULT_RIGHT_MARGIN;
            break;
        case centre:
            x += (width / 2) - (textBounds.width() / 2);
            break;
        }
        
        int h = ((box.height() / 2) - (Math.abs(textBounds.height()) / 2)) + Math.abs(textBounds.height());
        
        int y = box.top + h;
        canvas.drawText(text, x, y, paint);
        
        return box.height();
    }
}
