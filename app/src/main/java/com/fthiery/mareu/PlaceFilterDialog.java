package com.fthiery.mareu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.fthiery.mareu.model.Meeting;

public class PlaceFilterDialog extends DialogFragment {

    private EventListener mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Vérifie que l'activité hôte implémente l'interface de callback
        try {
            // Instancie le listener pour pouvoir renvoyer des évènements à l'hôte
            mainActivity = (EventListener) context;
        } catch (ClassCastException e) {
            // Si l'activité n'implèmente pas le listener, envoyer une exception
            throw new ClassCastException(context.toString()
                    + " must implement PlaceFilterDialog.Listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO: Utiliser un layout custom avec setView() pour afficher un textEdit avec une liste de suggestions
        // Initialisation de la boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.place_filter_dialog_title)
                .setItems(R.array.meeting_rooms, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // En cas de clic sur OK, renvoie au listener un String contenant le nom du lieu sélectionné
                        String[] places = getResources().getStringArray(R.array.meeting_rooms);
                        mainActivity.onPlaceFilterSelect(PlaceFilterDialog.this,places[which]);
                    }
                });

        // Création de la boîte de dialogue
        return builder.create();
    }
}
