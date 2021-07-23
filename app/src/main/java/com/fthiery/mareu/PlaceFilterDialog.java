package com.fthiery.mareu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PlaceFilterDialog extends DialogFragment {

    public interface Listener {
        public void onPlaceFilterSelect(DialogFragment dialog, String place);
    }

    Listener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (Listener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.place_filter_dialog_title)
                .setItems(R.array.meeting_rooms, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        String[] places = getResources().getStringArray(R.array.meeting_rooms);
                        listener.onPlaceFilterSelect(PlaceFilterDialog.this,places[which]);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
