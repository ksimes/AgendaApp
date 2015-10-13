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
        TextView field = (TextView) view.findViewById(resourceID);

        if (value != null) {
            field.setText(value);
        } else {
            field.setText("");
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
