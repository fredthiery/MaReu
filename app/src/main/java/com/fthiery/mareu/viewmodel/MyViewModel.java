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

    public static void addMeeting(long time, String place, String title, List<String> participants) {
        meetings.add(new Meeting(time, place, title, participants));
    }

    public static void addMeeting(long time, String place, String title, String participants) {
        List<String> p = Arrays.asList(participants.split(", "));
        meetings.add(new Meeting(time, place, title, p));
    }

    public static void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }
}
