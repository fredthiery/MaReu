package com.fthiery.mareu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fthiery.mareu.databinding.ActivityMainBinding;
import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.service.DummyMeetingList;
import com.fthiery.mareu.service.MeetingList;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements PlaceFilterDialog.EventListener {

    private static final int ADD_MEETING = 1;
    private ActivityMainBinding binding;
    private final MeetingList mMeetings = new DummyMeetingList();
    private LinearLayoutManager mManager;
    private MyMeetingRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gonflage du xml de l'activité
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialisation du RecyclerView
        mManager = new LinearLayoutManager(binding.meetingRecyclerView.getContext());
        mAdapter = new MyMeetingRecyclerViewAdapter(mMeetings);

        // En cas de clic sur le floatingActionButton, appel de l'activité addMeeting
        binding.fabAddMeeting.setOnClickListener(v -> {
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
            } else if (item.getItemId() == R.id.reset_filter) {
                mMeetings.setFilter();
                updateUI();
                return true;
            } else return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Configuration du RecyclerView
        binding.meetingRecyclerView.setLayoutManager(mManager);
        binding.meetingRecyclerView.setAdapter(mAdapter);
        mMeetings.setFilter();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Lorsque l'activité AddMeeting est finie, récupère les données dans un extra et appelle addMeeting()
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
        // Ajoute le meeting à la liste
        participants = participants.replaceAll("\\s+","").replace(",",", ");
        List<String> p = Arrays.asList(participants.split(", "));
        mMeetings.addMeeting(new Meeting(time, place, title, p));
    }

    private void updateUI() {
        mAdapter.notifyDataSetChanged();
        // Affiche ou masque le bouton de remise à zéro du filtre
        binding.topAppBar.getMenu().findItem(R.id.reset_filter).setVisible(mMeetings.isFiltered());
    }

    private void selectFilterDate() {
        // Fabrique un DateRangePicker pour sélectionner un intervalle de dates
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
        // Sélectionne la date du jour par défaut
        Pair<Long, Long> dateRange = Pair.create(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds());
        builder.setSelection(dateRange);

        MaterialDatePicker<Pair<Long, Long>> dateRangePicker = builder.build();

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            // Au clic sur Ok, applique le filtre à mMeetings et met à jour la RecyclerView
            int timeZoneOffset = TimeZone.getDefault().getOffset(new Date().getTime());
            mMeetings.setFilter(selection.first - timeZoneOffset, selection.second + 86400000L - timeZoneOffset);
            updateUI();
        });

        // Affiche la boite de dialogue
        dateRangePicker.show(getSupportFragmentManager(), "");
    }

    private void selectFilterPlace() {
        // Affiche un dialog de sélection de lieu
        DialogFragment placeFilterDialog = new PlaceFilterDialog();
        placeFilterDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onPlaceFilterSelect(DialogFragment dialog, String place) {
        // Au clic sur un lieu dans la liste, applique le filtre à mMeetings et met à jour la RecyclerView
        mMeetings.setFilter(place);
        updateUI();
    }
}