package com.fthiery.mareu.service;

import com.fthiery.mareu.model.Meeting;

import java.util.List;

/**
 * Meetings List which can be filtered by date or by place
 */
public interface MeetingList {

    /**
     * Get all the meetings
     *
     * @return {@link List}
     */
    List<Meeting> getMeetings();

    /**
     * Add a new meeting
     *
     * @param meeting Meeting to add
     */
    void addMeeting(Meeting meeting);

    /**
     * Delete the meeting
     *
     * @param meeting Meeting to delete
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Resets the filter
     */
    void setFilter();

    /**
     * Set a filter by date range
     *
     * @param startDate First date of the range
     * @param endDate   Last date of the range
     */
    void setFilter(Long startDate, Long endDate);

    /**
     * Set a filter by place
     *
     * @param place Place
     */
    void setFilter(String place);

    /**
     * Is the list filtered ?
     *
     * @return true if a filter is currently in place
     */
    boolean isFiltered();

    /**
     * Is the place available at the time
     *
     * @param place    Place
     * @param time     Time in millis
     * @param duration Duration in minutes
     * @return true if available
     */
    boolean isPlaceAvailableAt(String place, long time, int duration);

    /**
     * Returns the number of meetings (depending on the filter)
     * @return the number of meetings
     */
    int size();
}
