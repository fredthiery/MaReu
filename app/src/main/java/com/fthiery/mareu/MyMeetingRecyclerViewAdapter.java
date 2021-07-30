package com.fthiery.mareu;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.databinding.FragmentMeetingBinding;
import com.fthiery.mareu.service.MeetingList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting}.
 */
public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    private final MeetingList meetingList;

    public MyMeetingRecyclerViewAdapter(MeetingList meetings) {
        this.meetingList = meetings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Récupère les données à afficher depuis le Meeting correspondant et les formate
        Meeting meeting = meetingList.getMeetings().get(position);
        String title = meeting.getTitle();
        String place = meeting.getPlace();
        String participants = TextUtils.join(", ", meetingList.getMeetings().get(position).getParticipants());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(meeting.getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy' 'H'h'mm", Locale.getDefault());
        String time = format.format(cal.getTime());

        // Affiche les données dans les TextView
        holder.fullTitleTextView.setText(String.format("%s - %s - %s", title, time, place));
        holder.participantsTextView.setText(participants);
    }

    @Override
    public int getItemCount() {
        return meetingList.getMeetings().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView fullTitleTextView;
        public final TextView participantsTextView;

        public ViewHolder(FragmentMeetingBinding binding) {
            super(binding.getRoot());
            fullTitleTextView = binding.meetingFullTitle;
            participantsTextView = binding.meetingParticipants;

            binding.deleteButton.setOnClickListener(v -> {
                // Gestion du clic sur le bouton de suppression de réunion
                Meeting m = meetingList.getMeetings().get(getLayoutPosition());
                meetingList.deleteMeeting(m);
                notifyDataSetChanged();
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + fullTitleTextView.getText() + "'";
        }
    }
}