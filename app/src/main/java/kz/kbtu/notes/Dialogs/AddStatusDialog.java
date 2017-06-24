package kz.kbtu.notes.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import kz.kbtu.notes.R;

/**
 * Created by abakh on 24-Jun-17.
 */

public class AddStatusDialog extends DialogFragment {

    public interface AddStatusDialogListener{
        void onPositiveClick(DialogFragment dialog, String text);
        void onNegativeClick(DialogFragment dialog);

    }

    AddStatusDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddStatusDialogListener) context;
        }
        catch (ClassCastException e){

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.status_alert_dialog, null);
        final EditText statusText = (EditText)view.findViewById(R.id.etAddStatus);
        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = statusText.getText().toString();
                        listener.onPositiveClick(AddStatusDialog.this, text);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNegativeClick(AddStatusDialog.this);
                    }
                }).setTitle("Add status");

        return builder.create();
    }
}