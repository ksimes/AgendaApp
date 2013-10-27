package com.stronans.android.agenda.support;

import java.util.Calendar;

import android.graphics.Rect;
import android.graphics.RectF;

public class GridSelection
{
    public DateInfo dateOnStartOfGrid = null;
    private int top = 0;
    private int cellWidth = 0;
    private int numColumns = 0;
    private int cellHeight = 0;
    private int bottom = 0;

    /**
     * @return the top
     */
    public int getTop()
    {
        return top;
    }

    /**
     * @param top the top to set
     */
    public void setTop(int top)
    {
        this.top = top;
    }

    /**
     * @return the cellWidth
     */
    public int getCellWidth()
    {
        return cellWidth;
    }

    /**
     * @param cellWidth the cellWidth to set
     */
    public void setCellWidth(int cellWidth)
    {
        this.cellWidth = cellWidth;
    }

    /**
     * @return the numColumns
     */
    public int getNumColumns()
    {
        return numColumns;
    }

    /**
     * @param numColumns the numColumns to set
     */
    public void setNumColumns(int numColumns)
    {
        this.numColumns = numColumns;
    }

    /**
     * @return the cellHeight
     */
    public int getCellHeight()
    {
        return cellHeight;
    }

    /**
     * @param cellHeight the cellHeight to set
     */
    public void setCellHeight(int cellHeight)
    {
        this.cellHeight = cellHeight;
    }

    /**
     * @return the bottom
     */
    public int getBottom()
    {
        return bottom;
    }

    /**
     * @param bottom the bottom to set
     */
    public void setBottom(int bottom)
    {
        this.bottom = bottom;
    }
    
    public Rect getCellRect(int columns, int rows)
    {
        return new Rect(columns * cellWidth, 
            top + cellWidth * rows, 
            (columns * cellWidth) + cellWidth, 
            top + cellWidth + cellWidth * rows);
    }
    
    public RectF getCellRectF(int columns, int rows)
    {
        return new RectF(columns * cellWidth, 
            top + cellWidth * rows, 
            (columns * cellWidth) + cellWidth, 
            top + cellWidth + cellWidth * rows);
    }
    
    public DateInfo dateHit(int xPosition, int yPosition)
    {
        int cell = 0;
        DateInfo dateOnGrid = null;
        
        if((yPosition > top) && (yPosition < bottom))
        {
            cell = ((xPosition / cellWidth) + ((yPosition - top) / cellHeight) * numColumns); 
                
            dateOnGrid = new DateInfo(dateOnStartOfGrid.getDate());
            dateOnGrid.getCalendar().add(Calendar.DATE, cell);
        }
        
        return dateOnGrid;
    }
    
    /**
     * Returns the cell that has been selected
     * @param xPosition Mouse x position on display
     * @param yPosition Mouse y position on display
     * @return -1 if no cell was selected otherwise between 0 and the number of cells in the grid.
     */
    public int cellHit(int xPosition, int yPosition)
    {
        int cell = -1;
        
        if((yPosition > top) && (yPosition < bottom))
        {
            cell = ((xPosition / cellWidth) + ((yPosition - top) / cellHeight) * numColumns); 
        }
        
        return cell;
    }
    
    public Rect getSelectedCellRect(int columns, int rows)
    {
        Rect result = getCellRect(columns, rows);
        result.inset(1, 1);     // Expand by 1 all round
        return result;
    }
}
