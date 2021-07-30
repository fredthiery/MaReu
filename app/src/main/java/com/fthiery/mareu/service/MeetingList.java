package com.fthiery.mareu.service;

import com.fthiery.mareu.model.Meeting;

import java.util.List;

/**
 * Meetings API
 */
public interface MeetingList {

    /**
     * Get all the meetings
     * @return {@link List}
     */
    List<Meeting> getMeetings();

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

    /**
     * Resets the filter
     */
    void setFilter();

    /**
     * Set a filter by date range
     * @param startDate
     * @param endDate
     */
    void setFilter(Long startDate, Long endDate);

    /**
     * Set a filter by place
     * @param place
     */
    void setFilter(String place);

    /**
     * Returns the type of filter currently used
     * @return
     */
    boolean isFiltered();
}
