package com.stronans.android.agenda.support;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Utilities {

    private Utilities() {
    }

    public static boolean hasContent(String string) {
        boolean result = string != null;

        if (result)
            result = !string.isEmpty();

        return result;
    }

    public static void setTextView(View view, int resourceID, String value) {
        setTextView(view, resourceID, value, true);
    }

    public static void setTextView(View view, int resourceID, String value, boolean visibility) {
        TextView field = (TextView) view.findViewById(resourceID);

        if (value != null) {
            field.setText(value);
        } else {
            field.setText("");
        }

        if (visibility) {
            field.setVisibility(View.VISIBLE);
        } else {
            field.setVisibility(View.GONE);
        }
    }

    public static void textViewVisibility(View view, int resourceID, boolean status) {
        TextView field = (TextView) view.findViewById(resourceID);

        if (status) {
            field.setVisibility(View.VISIBLE);
        } else {
            field.setVisibility(View.GONE);
        }
    }

    public static void setButtonText(View view, int resourceID, String value) {
        Button field = (Button) view.findViewById(resourceID);

        if (value != null) {
            field.setText(value);
        } else {
            field.setText("");
        }
    }
}
