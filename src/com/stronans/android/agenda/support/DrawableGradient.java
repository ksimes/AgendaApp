package com.stronans.android.agenda.support;

import android.graphics.drawable.GradientDrawable;

public class DrawableGradient extends GradientDrawable {

    public DrawableGradient(int[] colors) {
        super(GradientDrawable.Orientation.LEFT_RIGHT, colors);

        try {
            this.setShape(GradientDrawable.RECTANGLE);
            this.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DrawableGradient SetTransparency(int transparencyPercent) {
        this.setAlpha(255 - ((255 * transparencyPercent) / 100));

        return this;
    }
}