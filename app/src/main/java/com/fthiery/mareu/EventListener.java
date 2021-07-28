package com.fthiery.mareu;

import androidx.fragment.app.DialogFragment;

import com.fthiery.mareu.model.Meeting;

public interface EventListener {
    void onPlaceFilterSelect(DialogFragment dialog, String place);

    void deleteMeeting(Meeting meeting);
}
