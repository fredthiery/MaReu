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
    private final Calendar dateTime = Calendar.getInstance(TimeZone.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        binding.meetingRoomEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.meeting_rooms, R.layout.list_item));

        binding.meetingParticipantsEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.participants, R.layout.list_item));
        binding.meetingParticipantsEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        binding.meetingDateEdit.setOnClickListener(v -> buttonSelectDate());
        binding.meetingTimeEdit.setOnClickListener(v -> buttonSelectTime());

        displayFormattedTime();
        displayFormattedDate();

        binding.newMeetingSaveButton.setOnClickListener(v -> saveMeeting());
    }

    private void buttonSelectDate() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());
        builder.setSelection(dateTime.getTimeInMillis());
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar newDate = Calendar.getInstance(TimeZone.getDefault());
            newDate.setTimeInMillis(selection);

            dateTime.set(newDate.get(Calendar.YEAR),newDate.get(Calendar.MONTH),newDate.get(Calendar.DAY_OF_MONTH));

            displayFormattedDate();
        });
        datePicker.show(getSupportFragmentManager(),"");
    }

    private void displayFormattedDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String formatted = format.format(dateTime.getTime());
        binding.meetingDateEdit.setText(formatted);
    }

    private void buttonSelectTime() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(dateTime.get(Calendar.HOUR_OF_DAY))
                .setMinute(dateTime.get(Calendar.MINUTE))
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            dateTime.set(Calendar.HOUR_OF_DAY,hour);
            dateTime.set(Calendar.MINUTE,minute);

            displayFormattedTime();
        });
        timePicker.show(getSupportFragmentManager(),"tag");
    }

    private void displayFormattedTime() {
        SimpleDateFormat format = new SimpleDateFormat("H'h'mm",Locale.getDefault());
        String formatted = format.format(dateTime.getTime());
        binding.meetingTimeEdit.setText(formatted);
    }

    private void saveMeeting() {
        Intent intent = new Intent();

        intent.putExtra("time", dateTime.getTimeInMillis());
        intent.putExtra("place",binding.meetingRoomEdit.getText().toString());
        intent.putExtra("title",binding.meetingTitleEdit.getText().toString());
        intent.putExtra("participants",binding.meetingParticipantsEdit.getText().toString());

        setResult(RESULT_OK,intent);
        finish();
    }
}