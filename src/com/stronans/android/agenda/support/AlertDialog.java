package com.stronans.android.agenda.support;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.stronans.android.agenda.R;

/**
 *
 * Created by S.King on 12/10/2015.
 */
public class AlertDialog extends DialogFragment {

    public interface DialogListener {
        void doPositiveClick();
        void doNegativeClick();
    }

    public static AlertDialog newInstance(String title, String message) {
        AlertDialog frag = new AlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");

        return new android.app.AlertDialog.Builder(getActivity())
                .setIcon(android.R.attr.alertDialogIcon)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((DialogListener) getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
    }
}