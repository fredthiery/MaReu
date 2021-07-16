package com.fthiery.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.fthiery.mareu.databinding.ActivityAddMeetingBinding;
import com.fthiery.mareu.viewmodel.MyViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding binding;
    private final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.meetingDateEdit.setOnClickListener(v -> buttonSelectDate());
        binding.meetingTimeEdit.setOnClickListener(v -> buttonSelectTime());
        binding.newMeetingSaveButton.setOnClickListener(v -> buttonSaveMeeting());

        binding.meetingRoomEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.meeting_rooms, R.layout.list_item));

        binding.meetingParticipantsEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.participants, R.layout.list_item));
        binding.meetingParticipantsEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        binding.topAppBar.setNavigationOnClickListener(v -> finish());
    }

    private void buttonSelectDate() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());
        builder.setSelection(cal.getTimeInMillis());
        builder.setCalendarConstraints(constraintsBuilder.build());
        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar newDate = Calendar.getInstance();
            newDate.setTimeInMillis(selection);

            cal.set(newDate.get(Calendar.YEAR),newDate.get(Calendar.MONTH),newDate.get(Calendar.DAY_OF_MONTH));

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.FRANCE);
            String formatted = format.format(cal.getTime());
            binding.meetingDateEdit.setText(formatted);
        });
        datePicker.show(getSupportFragmentManager(),"tag");
    }

    private void buttonSelectTime() {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(cal.get(Calendar.HOUR_OF_DAY))
                .setMinute(cal.get(Calendar.MINUTE))
                .build();

        timePicker.addOnPositiveButtonClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            cal.set(Calendar.HOUR_OF_DAY,hour);
            cal.set(Calendar.MINUTE,minute);

            SimpleDateFormat format = new SimpleDateFormat("H'h'mm",Locale.FRANCE);
            String formatted = format.format(cal.getTime());
            binding.meetingTimeEdit.setText(formatted);
        });
        timePicker.show(getSupportFragmentManager(),"tag");
    }

    private void buttonSaveMeeting() {
        Long time = cal.getTimeInMillis();
        String place = binding.meetingRoomEdit.getText().toString();
        String title = binding.meetingTitleEdit.getText().toString();
        String participants = binding.meetingParticipantsEdit.getText().toString();
        MyViewModel.addMeeting(time,place,title,participants);
        finish();
    }
}