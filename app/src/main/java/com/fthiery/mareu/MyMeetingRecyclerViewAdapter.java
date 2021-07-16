package com.fthiery.mareu;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.databinding.FragmentMeetingBinding;
import com.fthiery.mareu.viewmodel.MyViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting}.
 */
public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    private final List<Meeting> meetings;

    public MyMeetingRecyclerViewAdapter(List<Meeting> items) {
        meetings = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        String title = meeting.getTitle();
        String place = meeting.getPlace();
        String participants = TextUtils.join(", ",meetings.get(position).getParticipants());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(meeting.getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy' 'H'h'mm", Locale.FRANCE);
        String time = format.format(cal.getTime());

        holder.fullTitleTextView.setText(title+" - "+time+" - "+place);
        holder.participantsTextView.setText(participants);

        holder.deleteButton.setOnClickListener(v -> {
            MyViewModel.deleteMeeting(meeting);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView fullTitleTextView;
        public final TextView participantsTextView;
        public final ImageButton deleteButton;

        public ViewHolder(FragmentMeetingBinding binding) {
            super(binding.getRoot());
            fullTitleTextView = binding.meetingFullTitle;
            participantsTextView = binding.meetingParticipants;
            deleteButton = binding.deleteButton;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + fullTitleTextView.getText() + "'";
        }
    }
}