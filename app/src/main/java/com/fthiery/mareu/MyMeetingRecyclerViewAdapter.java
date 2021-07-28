package com.fthiery.mareu;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.databinding.FragmentMeetingBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting}.
 */
public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    // meetings contient nos données
    private final List<Meeting> meetings;

    public MyMeetingRecyclerViewAdapter(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent.getContext(),FragmentMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Récupère les données à afficher depuis le Meeting correspondant et les formate
        Meeting meeting = meetings.get(position);
        String title = meeting.getTitle();
        String place = meeting.getPlace();
        String participants = TextUtils.join(", ", meetings.get(position).getParticipants());

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
        return meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView fullTitleTextView;
        public final TextView participantsTextView;
        private final EventListener mainActivity;

        public ViewHolder(Context context, FragmentMeetingBinding binding) {
            super(binding.getRoot());
            mainActivity = (EventListener) context;
            fullTitleTextView = binding.meetingFullTitle;
            participantsTextView = binding.meetingParticipants;

            binding.deleteButton.setOnClickListener(v -> {
                // Gestion du clic sur le bouton de suppression de réunion
                mainActivity.deleteMeeting(meetings.get(getLayoutPosition()));
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + fullTitleTextView.getText() + "'";
        }
    }
}