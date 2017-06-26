package kz.kbtu.notes.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

/**
 * Created by abakh on 26-Jun-17.
 */

public class NoteLongClickDialog extends DialogFragment {

    public interface NoteDialogListener{
        void actionClicked(int which, int adapterPosition);
    }

    NoteDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (NoteDialogListener)context;
        }
        catch (ClassCastException e){

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);
        arrayAdapter.add("Delete");
        arrayAdapter.add("Share");
        Bundle bundle = getArguments();
        final int position = bundle.getInt("position");

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.actionClicked(which, position);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getListView().setDivider(new ColorDrawable(Color.GRAY));
        dialog.getListView().setDividerHeight(5);

        return dialog;
    }
}
