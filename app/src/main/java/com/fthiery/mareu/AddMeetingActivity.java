package com.fthiery.mareu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.fthiery.mareu.databinding.ActivityAddMeetingBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding binding;
    // dateTime va nous servir à stocker la date et l'heure sélectionnées. On l'initialise avec la date et l'heure actuelles
    private final Calendar dateTime = Calendar.getInstance(TimeZone.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gonflage du xml de l'activité
        binding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // En cas de clic sur le bouton Up, fin de l'activité et retour à la précédente
        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        // Liste de suggestions pour le TextEdit meetingRoomEdit
        binding.meetingRoomEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.meeting_rooms, R.layout.list_item));

        // Liste de suggestions pour le TextEdit meetingParticipantsEdit
        binding.meetingParticipantsEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.participants, R.layout.list_item));
        binding.meetingParticipantsEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // En cas de clic sur la date ou l'heure, appel de la méthode correspondante
        binding.meetingDateEdit.setOnClickListener(v -> buttonSelectDate());
        binding.meetingTimeEdit.setOnClickListener(v -> buttonSelectTime());

        // Affichage de la date et de l'heure actuelles dans les champs correspondants
        displayFormattedTime();
        displayFormattedDate();

        // En cas de clic sur le bouton de sauvegarde, appel de la méthode saveMeeting()
        binding.newMeetingSaveButton.setOnClickListener(v -> saveMeeting());
    }

    private void displayFormattedDate() {
        // Formatte dateTime pour l'afficher sous forme de date dans le champ correspoondant
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String formatted = format.format(dateTime.getTime());
        binding.meetingDateEdit.setText(formatted);
    }

    private void displayFormattedTime() {
        // Formatte dateTime pour l'afficher sous forme d'heure' dans le champ correspoondant
        SimpleDateFormat format = new SimpleDateFormat("H'h'mm",Locale.getDefault());
        String formatted = format.format(dateTime.getTime());
        binding.meetingTimeEdit.setText(formatted);
    }

    private void buttonSelectDate() {
        // Initialisation du MaterialDatePicker afin de sélectionner la date
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

        // Ajoute comme contrainte l'obligation de sélectionner un date dans l'avenir
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());

        // Sélectionne la date actuelle
        builder.setSelection(dateTime.getTimeInMillis());

        // Application des contraintes et création du MaterialDatePicker
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Long> datePicker = builder.build();

        // Gestion du clic sur le bouton OK
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Récupère la date sélectionnée sous forme de Long, la transforme en Calendar, puis applique ses propriétés YEAR, MONTH et DAY_OF_MONTH à dateTime
            Calendar newDate = Calendar.getInstance(TimeZone.getDefault());
            newDate.setTimeInMillis(selection);

            dateTime.set(newDate.get(Calendar.YEAR),newDate.get(Calendar.MONTH),newDate.get(Calendar.DAY_OF_MONTH));

            // Affiche la nouvelle date dans le champ meeting_date_edit
            displayFormattedDate();
        });

        // Affichage du MaterialDatePicker
        datePicker.show(getSupportFragmentManager(),"");
    }

    private void buttonSelectTime() {
        // Initialisation du MaterialDatePicker
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(dateTime.get(Calendar.HOUR_OF_DAY))
                .setMinute(dateTime.get(Calendar.MINUTE))
                .build();

        // Gestion du clic sur le bouton OK
        timePicker.addOnPositiveButtonClickListener(v -> {
            // Récupère l'heure sélectionnée et l'applique à dateTime
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            dateTime.set(Calendar.HOUR_OF_DAY,hour);
            dateTime.set(Calendar.MINUTE,minute);

            // Affiche la nouvelle heure dans le champ meeting_time_edit
            displayFormattedTime();
        });

        // Affichage du MaterialTimePicker
        timePicker.show(getSupportFragmentManager(),"tag");
    }

    private void saveMeeting() {
        // TODO: Vérifier que tous les champs sont remplis et afficher un toast si ce n'est pas le cas
        // Ajoute les données des champs dans des Extra pour les retransmettre à l'activité parente via setResult()
        Intent intent = new Intent();

        intent.putExtra("time", dateTime.getTimeInMillis());
        intent.putExtra("place",binding.meetingRoomEdit.getText().toString());
        intent.putExtra("title",binding.meetingTitleEdit.getText().toString());
        intent.putExtra("participants",binding.meetingParticipantsEdit.getText().toString());

        setResult(RESULT_OK,intent);

        // Termine l'activité
        finish();
    }
}