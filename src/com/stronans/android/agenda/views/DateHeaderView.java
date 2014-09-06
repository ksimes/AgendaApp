package com.stronans.android.agenda.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import com.stronans.android.agenda.activities.DateHeaderDisplay;
import com.stronans.android.agenda.dataaccess.AgendaStaticData;
import com.stronans.android.agenda.enums.FormatStyle;
import com.stronans.android.agenda.interfaces.Refresher;
import com.stronans.android.agenda.model.AgendaConfiguration;
import com.stronans.android.agenda.model.DateInfo;
import com.stronans.android.agenda.support.DrawableGradient;
import com.stronans.android.agenda.support.FormattedInfo;
import com.stronans.android.agenda.support.ResourceInfo;

public class DateHeaderView extends View implements OnClickListener {
    AgendaConfiguration config;
    DateInfo selectedDate;
    Paint paint;
    DrawableGradient gradient;
    Rect mRect;
    Refresher refresher;
    Context context;

    // Used when inflated from a layout
    public DateHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.config = AgendaStaticData.getStaticData().getConfig();
        this.context = context;

        mRect = new Rect();
        paint = new Paint();
        paint.setAntiAlias(true);

        selectedDate = config.getDateInfo();
        // Get the colours for the graduated header box
        int[] colourRange = AgendaStaticData.getStaticData().monthColourRange(selectedDate);
        gradient = new DrawableGradient(colourRange);

        setOnClickListener(this);
    }

    // Only used when called from another activity
    public DateHeaderView(Context context) {
        this(context, null);
    }

    /**
     * Displays a subsidiary dialog which shows details associated with this date.
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), DateHeaderDisplay.class);

        intent.putExtra("DateInfo", selectedDate.getMilliseconds());

        context.startActivity(intent);
    }

    /**
     * Sets the selected date to display in this header label
     *
     * @param newdate
     */
    public void setNewDate(DateInfo newdate) {
        selectedDate = DateInfo.fromDateInfo(newdate);
        // Get the colours for the graduated header box
        int[] colourRange = AgendaStaticData.getStaticData().monthColourRange(selectedDate);
        gradient = new DrawableGradient(colourRange);

        requestLayout();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int shift = 0;
        int width = getWidth();
        float height = getHeight();

        // Scale text to percentages of the already scaled header.
        int intervalSize = (int) (height * 0.30);
        int titleSize = (int) (height * 0.50);

        gradient.setBounds(0, 0, width, (int) height);
        gradient.draw(canvas);

        // Get and draw out the interval text (left justified)
        String intervalString = ResourceInfo.getIntervalString(selectedDate, getResources(), FormatStyle.longStyle);

        paint.setTextSize(intervalSize);
        shift += Math.round(Math.abs(paint.ascent()));
        canvas.drawText(intervalString, getPaddingLeft(), shift, paint);
        shift += paint.descent();

        // Then the date selected in the format selected in the orientation selected.
        paint.setTextSize(titleSize);
        String dateString = DateInfo.getDateString(selectedDate);
        paint.getTextBounds(dateString, 0, dateString.length(), mRect);
        int x = 0;
        switch (config.getHeaderOrientation()) {
            case left:
                x = getPaddingLeft();
                break;
            case right:
                x = width - mRect.right - getPaddingRight();
                break;
            case centre:
                x = (width / 2) - (mRect.right / 2);
                break;
        }
        shift += Math.round(Math.abs(paint.ascent()));
        canvas.drawText(dateString, x, shift, paint);
    }
}
