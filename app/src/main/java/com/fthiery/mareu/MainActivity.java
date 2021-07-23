package com.fthiery.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fthiery.mareu.databinding.ActivityMainBinding;
import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.service.DummyMeetingApiService;
import com.fthiery.mareu.service.MeetingApiService;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements PlaceFilterDialog.Listener {

    private static final int ADD_MEETING = 1;
    private ActivityMainBinding binding;
    private final MeetingApiService meetings = new DummyMeetingApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gonflage du xml de l'activité
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fabAddMeeting.setOnClickListener(v ->  {
            Intent intent = new Intent(getApplicationContext(), AddMeetingActivity.class);
            startActivityForResult(intent, ADD_MEETING);
        });

        // Configuration des actions du menu
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.filter_by_date) {
                selectFilterDate();
                return true;
            } else if (item.getItemId() == R.id.filter_by_place) {
                selectFilterPlace();
                return true;
            } else return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Configuration du RecyclerView
        binding.meetingRecyclerView.setLayoutManager(new LinearLayoutManager(binding.meetingRecyclerView.getContext()));
        binding.meetingRecyclerView.setAdapter(new MyMeetingRecyclerViewAdapter(meetings.getMeetings()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MEETING) {
            if (resultCode == RESULT_OK) {
                long time = data.getLongExtra("time", 0);
                String place = data.getStringExtra("place");
                String title = data.getStringExtra("title");
                String participants = data.getStringExtra("participants");
                addMeeting(time, place, title, participants);
            }
        }
    }

    public void addMeeting(long time, String place, String title, String participants) {
        List<String> p = Arrays.asList(participants.split(", "));
        meetings.addMeeting(new Meeting(time, place, title, p));
    }

    private void selectFilterDate() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
        Pair<Long,Long> dateRange = Pair.create(MaterialDatePicker.todayInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds());
        builder.setSelection(dateRange);
        MaterialDatePicker<Pair<Long,Long>> dateRangePicker = builder.build();

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            int timeZoneOffset = TimeZone.getDefault().getOffset(new Date().getTime());
            binding.meetingRecyclerView.setAdapter(new MyMeetingRecyclerViewAdapter(meetings.getMeetings(selection.first-timeZoneOffset,selection.second+86400000L-timeZoneOffset)));
        });

        dateRangePicker.show(getSupportFragmentManager(),"");
    }

    private void selectFilterPlace() {
        DialogFragment placeFilterDialog = new PlaceFilterDialog();
        placeFilterDialog.show(getSupportFragmentManager(),"dialog");

    }

    @Override
    public void onPlaceFilterSelect(DialogFragment dialog, String place) {
        Log.i("Filter", "onPlaceFilterSelect: " + place);
        binding.meetingRecyclerView.setAdapter(new MyMeetingRecyclerViewAdapter(meetings.getMeetings(place)));
    }
}