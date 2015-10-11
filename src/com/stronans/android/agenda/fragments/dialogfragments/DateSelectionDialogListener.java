package com.stronans.android.agenda.fragments.dialogfragments;

import com.stronans.android.agenda.model.DateInfo;

/**
 *
 * Created by S.King on 11/10/2015.
 */
public interface DateSelectionDialogListener {

    void onFinishDateSelection(DateInfo dateInfo, String id);
}
