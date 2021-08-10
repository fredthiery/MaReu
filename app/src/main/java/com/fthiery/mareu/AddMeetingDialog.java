package com.fthiery.mareu;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.fthiery.mareu.databinding.DialogAddMeetingBinding;
import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.service.MeetingList;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.TimeZone;

public class AddMeetingDialog extends DialogFragment {

    private DialogAddMeetingBinding binding;

    private Meeting newMeeting;
    private final MeetingList meetingList;
    private final Listener listener;

    public AddMeetingDialog(MeetingList mList, Listener listener) {
        this.meetingList = mList;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar dateTime = Calendar.getInstance(TimeZone.getDefault());
        newMeeting = new Meeting(dateTime.getTimeInMillis(), "", "", "", 45);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.myFullscreenAlertDialogStyle);
        binding = DialogAddMeetingBinding.inflate(getLayoutInflater());

        builder.setTitle(R.string.new_meeting_label);
        builder.setView(binding.getRoot());

        // Durée par défaut
        binding.meetingDurationEdit.setText("45");

        // Liste de suggestions pour le TextEdit meetingRoomEdit
        binding.meetingRoomEdit.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.meeting_rooms, R.layout.list_item));

        // Liste de suggestions pour le TextEdit meetingParticipantsEdit
        binding.meetingParticipantsEdit.setAdapter(ArrayAdapter.createFromResource(getContext(), R.array.participants, R.layout.list_item));
        binding.meetingParticipantsEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // En cas de clic sur la date ou l'heure, appel de la méthode correspondante
        binding.meetingDateEdit.setOnClickListener(v -> buttonSelectDate());
        binding.meetingTimeEdit.setOnClickListener(v -> buttonSelectTime());

        // Remplissage des champs de date et heure
        updateDateEdit();
        updateTimeEdit();

        builder.setPositiveButton(R.string.save_button, (dialog, id) -> {
            // En cas de clic sur Sauvegarder, ajouter une réunion
            listener.onAddMeeting(newMeeting);
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            // En cas de clic sur Annuler, ne rien faire
        });

        // Active un TextWatcher sur les champs pour activer le bouton de sauvegarde et afficher des messages d'erreur
        binding.meetingTitleEdit.addTextChangedListener(new TitleWatcher());

        MeetingRoomWatcher roomWatcher = new MeetingRoomWatcher();
        binding.meetingRoomEdit.addTextChangedListener(roomWatcher);
        binding.meetingDateEdit.addTextChangedListener(roomWatcher);
        binding.meetingTimeEdit.addTextChangedListener(roomWatcher);
        binding.meetingDurationEdit.addTextChangedListener(roomWatcher);

        binding.meetingParticipantsEdit.addTextChangedListener(new ParticipantsWatcher());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        activateSaveButton();
    }

    private void buttonSelectDate() {
        // Initialisation du MaterialDatePicker afin de sélectionner la date
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

        // Ajoute comme contrainte l'obligation de sélectionner un date dans l'avenir
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());

        // Sélectionne la date actuelle
        builder.setSelection(newMeeting.getTime());

        // Application des contraintes et création du MaterialDatePicker
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Long> datePicker = builder.build();

        // Gestion du clic sur le bouton OK
        datePicker.addOnPositiveButtonClickListener(selection -> {

            // Récupère la date sélectionnée sous forme de Long et l'applique à newMeeting
            newMeeting.setDate(selection);

            updateDateEdit();
        });

        // Affichage du MaterialDatePicker
        datePicker.show(getActivity().getSupportFragmentManager(), "");
    }

    private void buttonSelectTime() {
        // Initialisation du MaterialDatePicker
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(newMeeting.getDateTime().get(Calendar.HOUR_OF_DAY))
                .setMinute(newMeeting.getDateTime().get(Calendar.MINUTE))
                .build();

        // Gestion du clic sur le bouton OK
        timePicker.addOnPositiveButtonClickListener(v -> {
            // Récupère l'heure sélectionnée et l'applique à newMeeting
            newMeeting.setTime(timePicker.getHour(), timePicker.getMinute());

            updateTimeEdit();
        });

        // Affichage du MaterialTimePicker
        timePicker.show(getActivity().getSupportFragmentManager(), "tag");
    }

    private void updateDateEdit() {
        // Affiche la nouvelle date dans le champ meeting_date_edit
        binding.meetingDateEdit.setText(newMeeting.getFormattedDate());
    }

    private void updateTimeEdit() {
        // Affiche l'heure dans le champ meeting_time_edit
        binding.meetingTimeEdit.setText(newMeeting.getFormattedTime());
    }

    private boolean isValidEmail(String participants) {
        participants = participants.replaceAll("\\s+", "").replace(",", ", ");
        if (TextUtils.isEmpty(participants)) return false;
        String[] pList = participants.split(", ");
        for (String p : pList) {
            if (!Patterns.EMAIL_ADDRESS.matcher(p).matches()) return false;
        }
        return true;
    }

    private void activateSaveButton() {
        // Vérifie que tous les champs sont correctement remplis et active le bouton de sauvegarde
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            saveButton.setEnabled(
                    !newMeeting.getTitle().isEmpty()
                            && !newMeeting.getPlace().isEmpty()
                            && isValidEmail(binding.meetingParticipantsEdit.getText().toString())
            );
        }
    }

    private class TitleWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            newMeeting.setTitle(charSequence.toString());
            activateSaveButton();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class MeetingRoomWatcher extends TitleWatcher {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            newMeeting.setPlace(binding.meetingRoomEdit.getText().toString());

            String d = binding.meetingDurationEdit.getText().toString();
            if (!d.equals("")) newMeeting.setDuration(Integer.parseInt(d));

            if (!meetingList.isPlaceAvailableAt(newMeeting.getPlace(), newMeeting.getTime(), newMeeting.getDuration())) {
                binding.meetingRoomTextInput.setError(getString(R.string.place_unavailable));
            } else {
                binding.meetingRoomTextInput.setError("");
            }
            activateSaveButton();
        }
    }

    private class ParticipantsWatcher extends TitleWatcher {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Affiche un message d'erreur si le texte ne correspond pas à une adresse email
            if (!isValidEmail(charSequence.toString())) {
                binding.meetingParticipantsTextInput.setError(getString(R.string.email_address_error));
            } else {
                newMeeting.setParticipants(charSequence.toString());
                binding.meetingParticipantsTextInput.setError("");
            }
            activateSaveButton();
        }
    }
}
