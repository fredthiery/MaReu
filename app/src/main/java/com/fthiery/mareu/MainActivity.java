package com.fthiery.mareu;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fthiery.mareu.databinding.ActivityMainBinding;
import com.fthiery.mareu.databinding.LayoutPlaceFilterDialogBinding;
import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.service.DummyMeetingList;
import com.fthiery.mareu.service.MeetingList;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements Listener{

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
        mAdapter = new MyMeetingRecyclerViewAdapter(mMeetings, this);

        // En cas de clic sur le floatingActionButton, appel de l'activité addMeeting
        binding.fabAddMeeting.setOnClickListener(v -> {
            // Affichage de la boîte de dialogue d'ajout de réunion
            AddMeetingDialog dialog = new AddMeetingDialog(mMeetings, this);
            dialog.show(getSupportFragmentManager(), "dialog");
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
    public void onDeleteMeeting(int m) {
        mMeetings.deleteMeeting(mMeetings.getMeetings().get(m));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddMeeting(Meeting meeting) {
        mMeetings.addMeeting(meeting);
        mAdapter.notifyItemInserted(mMeetings.size());
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
        AlertDialog dialog = getPlaceFilterDialog();
        dialog.show();
    }

    private void onPlaceFilterSelect(String place) {
        // Au clic sur un lieu dans la liste, applique le filtre à mMeetings et met à jour la RecyclerView
        mMeetings.setFilter(place);
        updateUI();
    }

    private AlertDialog getPlaceFilterDialog() {
        // Initialisation de la boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutPlaceFilterDialogBinding dialogBinding = LayoutPlaceFilterDialogBinding.inflate(getLayoutInflater());
        // Remplit la liste de suggestions
        dialogBinding.dialogMeetingRoomEdit.setAdapter(ArrayAdapter.createFromResource(this, R.array.meeting_rooms, R.layout.list_item));

        builder.setTitle(R.string.place_filter_dialog_title);
        builder.setView(dialogBinding.getRoot());
        builder.setPositiveButton(R.string.ok, (dialog, id) -> {
            // En cas de clic sur OK, renvoie au listener un String contenant le nom du lieu sélectionné
            onPlaceFilterSelect(dialogBinding.dialogMeetingRoomEdit.getText().toString());
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            // En cas de clic sur Annuler, ne rien faire
        });

        return builder.create();
    }
}