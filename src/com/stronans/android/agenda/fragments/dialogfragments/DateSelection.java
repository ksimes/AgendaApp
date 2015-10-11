package com.stronans.android.agenda.fragments.dialogfragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import com.stronans.android.agenda.model.DateInfo;

/**
 *
 * Created by S.King on 10/10/2015.
 */
public class DateSelection extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private DateInfo dateInfo;
    private String selectionId = "";

    /**
     * Create a new instance of DateSelection, providing a DateInfo
     * as an argument along with a id for distingishing between different dialogs.
     */
    static public DateSelection newInstance(DateInfo dateInfo, String id) {
        DateSelection f = new DateSelection();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("Date", DateInfo.toUniversalString(dateInfo));
        args.putString("Id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String data = getArguments().getString("Date");
        if (data != null)
            dateInfo = DateInfo.fromUniversalString(data);
        else
            dateInfo = DateInfo.getNow();

        data = getArguments().getString("Id");
        if (data != null)
            selectionId = data;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, dateInfo.getYear(), dateInfo.getMonth(), dateInfo.getDateInMonth());
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        DateSelectionDialogListener activity = (DateSelectionDialogListener) getActivity();
        activity.onFinishDateSelection(DateInfo.fromDate(day, month, year), selectionId);
//        this.dismiss();
    }
}
