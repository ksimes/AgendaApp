package com.stronans.android.agenda.support;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.stronans.android.agenda.R;

/**
 *
 * Created by S.King on 12/10/2015.
 */
public class AgendaAlertDialog extends DialogFragment {

    public interface DialogListener {
        void doPositiveClick();
        void doNegativeClick();
    }

    public static AgendaAlertDialog newInstance(String title, String message, Boolean positiveMessage, Boolean negativeMessage) {
        AgendaAlertDialog frag = new AgendaAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putBoolean("positive", positiveMessage);
        args.putBoolean("negative", negativeMessage);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        Boolean positiveMessage = getArguments().getBoolean("positive");
        Boolean negativeMessage = getArguments().getBoolean("negative");

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setIcon(android.R.attr.alertDialogIcon)
                .setTitle(title)
                .setMessage(message);

        if(positiveMessage) {
            dialog.setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((DialogListener) getActivity()).doPositiveClick();
                        }
                    }
            );
        }

        if(negativeMessage) {
            dialog.setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((DialogListener) getActivity()).doNegativeClick();
                        }
                    }
            );
        }

        return dialog.create();
    }
}