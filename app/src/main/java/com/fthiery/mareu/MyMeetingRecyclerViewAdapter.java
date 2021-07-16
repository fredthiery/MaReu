package com.fthiery.mareu;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.databinding.FragmentMeetingBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting}.
 */
public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    private final List<Meeting> mMeetings;

    public MyMeetingRecyclerViewAdapter(List<Meeting> items) {
        mMeetings = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mMeetings.get(position);
        holder.mFullTitle.setText(holder.mItem.getTitle() +" - "+ holder.mItem.getTime() +" - "+ holder.mItem.getPlace());
        holder.mParticipants.setText(mMeetings.get(position).getParticipants().toString());
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mFullTitle;
        public final TextView mParticipants;
        public Meeting mItem;

        public ViewHolder(FragmentMeetingBinding binding) {
            super(binding.getRoot());
            mFullTitle = binding.meetingFullTitle;
            mParticipants = binding.meetingParticipants;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFullTitle.getText() + "'";
        }
    }
}