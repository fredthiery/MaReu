package com.fthiery.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.fthiery.mareu.databinding.ActivityMainBinding;
import com.fthiery.mareu.viewmodel.MyViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gonflage du xml de l'activité
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fabAddMeeting.setOnClickListener(v -> {
            // Appel de l'activité AddMeetingActivity
            Intent intent = new Intent(getApplicationContext(),AddMeetingActivity.class);
            startActivity(intent);
        });

        // Gestion des clics sur l'appBar
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.filter_menu_item) {
                return true;
            } else return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Configuration du RecyclerView
        binding.meetingRecyclerView.setLayoutManager(new LinearLayoutManager(binding.meetingRecyclerView.getContext()));
        binding.meetingRecyclerView.setAdapter(new MyMeetingRecyclerViewAdapter(MyViewModel.getMeetings()));
    }
}