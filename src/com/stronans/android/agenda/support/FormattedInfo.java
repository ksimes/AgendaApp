package com.stronans.android.agenda.support;

import java.text.SimpleDateFormat;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.stronans.android.agenda.model.Incident;

public class FormattedInfo
{
    static private String formatInfo(DateInfo timeInfo, String format)
    {
        SimpleDateFormat output = new SimpleDateFormat(format); 
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
        sb.append(event.getTitle());

        if(!event.isAllDay())
        {
            sb.append("  [");
            sb.append(FormattedInfo.getTimeString(event.getStart()));
            sb.append(" - ");
            sb.append(FormattedInfo.getTimeString(event.getEnd()));
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
    static public int drawWrappedText(String text, int x, int y, float width, Paint paint, Canvas canvas)
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
}
