package com.fthiery.mareu.service;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.repository.DummyMeetingRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy implementation of the API
 */
public class DummyMeetingApiService implements MeetingApiService {

    List<Meeting> meetings = DummyMeetingRepo.generateMeetings();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetings(Long startDate, Long endDate) {
        List<Meeting> filteredList = new ArrayList<>();
        for (Meeting m: meetings) {
            if (m.getTime()>=startDate && m.getTime()<=endDate) {
                filteredList.add(m);
            }
        }
        return filteredList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetings(String meetingRoom) {
        List<Meeting> filteredList = new ArrayList<>();
        for (Meeting m: meetings) {
            if (m.getPlace()==meetingRoom) {
                filteredList.add(m);
            }
        }
        return filteredList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }
}
