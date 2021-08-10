package com.fthiery.mareu;

import com.fthiery.mareu.model.Meeting;

public interface Listener {
    void onDeleteMeeting(int m);

    void onAddMeeting(Meeting meeting);
}
