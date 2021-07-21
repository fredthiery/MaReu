package com.fthiery.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fthiery.mareu.databinding.ActivityMainBinding;
import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.repository.DummyMeetingRepo;
import com.fthiery.mareu.service.DummyMeetingApiService;
import com.fthiery.mareu.service.MeetingApiService;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MeetingApiService meetings = new DummyMeetingApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gonflage du xml de l'activité
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Gestion des clics sur l'appBar
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.filter_menu_item) {
                return true;
                // lancer un dialog
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
        if (requestCode == 1) {
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

    public void startAddMeetingActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddMeetingActivity.class);
        startActivityForResult(intent, 1);
    }
}