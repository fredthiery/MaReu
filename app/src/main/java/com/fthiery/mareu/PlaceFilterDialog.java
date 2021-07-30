package com.fthiery.mareu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.fthiery.mareu.databinding.LayoutPlaceFilterDialogBinding;

public class PlaceFilterDialog extends DialogFragment {

    public interface EventListener {
        void onPlaceFilterSelect(DialogFragment dialog, String place);
    }

    private EventListener mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Initialisation de la boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutPlaceFilterDialogBinding binding = LayoutPlaceFilterDialogBinding.inflate(getLayoutInflater());
        // Remplit la liste de suggestions
        binding.meetingRoomEdit.setAdapter(ArrayAdapter.createFromResource(this.getContext(), R.array.meeting_rooms, R.layout.list_item));

        builder.setTitle(R.string.place_filter_dialog_title)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    // En cas de clic sur OK, renvoie au listener un String contenant le nom du lieu sélectionné
                    mainActivity.onPlaceFilterSelect(PlaceFilterDialog.this, binding.meetingRoomEdit.getText().toString());
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // En cas de clic sur Annuler, ne rien faire
                });

        // Création de la boîte de dialogue
        return builder.create();
    }
}
