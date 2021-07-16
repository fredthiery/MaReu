package com.fthiery.mareu.viewmodel;

import androidx.lifecycle.ViewModel;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.repository.DummyMeetingRepo;

import java.util.Arrays;
import java.util.List;

public class MyViewModel extends ViewModel {
    private static List<Meeting> meetings = DummyMeetingRepo.generateMeetings();

    public static List<Meeting> getMeetings() {
        return meetings;
    }

    public static void addMeeting() {
        meetings.add(new Meeting( System.currentTimeMillis(),
                "Salle Luigi",
                "Réunion inutile",
                Arrays.asList("pierre.quiroule@lamzone.com","lucie.dubois@lamzone.com")
        ));
    }
}
