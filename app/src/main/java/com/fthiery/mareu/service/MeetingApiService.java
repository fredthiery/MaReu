package com.fthiery.mareu.service;

import com.fthiery.mareu.model.Meeting;

import java.util.List;

/**
 * Meetings API
 */
public interface MeetingApiService {

    /**
     * Get all the meetings
     * @return {@link List}
     */
    List<Meeting> getMeetings();

    /**
     * Get all the meetings between startDate and endDate
     * @param startDate
     * @param endDate
     * @return
     */
    List<Meeting> getMeetings(Long startDate, Long endDate);

    /**
     * Get all the meetings in the meetingRoom
     * @param meetingRoom
     * @return
     */
    List<Meeting> getMeetings(String meetingRoom);

    /**
     * Add a new meeting
     * @param meeting
     */
    void addMeeting(Meeting meeting);

    /**
     * Delete the meeting
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

}
