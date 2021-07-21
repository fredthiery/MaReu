package com.fthiery.mareu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class AddMeetingActivity extends AppCompatActivity {

    private ActivityAddMeetingBinding binding;
    private final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.meetingRoomEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.meeting_rooms, R.layout.list_item));

        binding.meetingParticipantsEdit.setAdapter(ArrayAdapter.createFromResource(this,R.array.participants, R.layout.list_item));
        binding.meetingParticipantsEdit.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        binding.topAppBar.setNavigationOnClickListener(v -> finish());
    }

    public void buttonSelectDate(View view) {
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

    public void buttonSelectTime(View view) {
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

    public void saveMeeting(View view) {
        Intent intent = new Intent();

        intent.putExtra("time",cal.getTimeInMillis());
        intent.putExtra("place",binding.meetingRoomEdit.getText().toString());
        intent.putExtra("title",binding.meetingTitleEdit.getText().toString());
        intent.putExtra("participants",binding.meetingParticipantsEdit.getText().toString());

        setResult(RESULT_OK,intent);
        finish();
    }
}