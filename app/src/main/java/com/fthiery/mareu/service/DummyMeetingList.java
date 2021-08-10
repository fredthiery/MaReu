package com.fthiery.mareu.service;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.repository.DummyMeetingRepo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Implementation of the interface
 */
public class DummyMeetingList implements MeetingList {

    public static final int NONE = 0;
    public static final int DATE = 1;
    public static final int PLACE = 2;

    private final List<Meeting> meetings = DummyMeetingRepo.generateMeetings();
    public List<Meeting> filteredMeetings = new ArrayList<>(meetings);
    private Filter filter = new Filter();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetings() {
        return filteredMeetings;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
        refreshFilteredMeetings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
        filteredMeetings.remove(meeting);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilter() {
        filter = new Filter();
        refreshFilteredMeetings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilter(Long startDate, Long endDate) {
        filter = new Filter(startDate, endDate);
        refreshFilteredMeetings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilter(String place) {
        filter = new Filter(place);
        refreshFilteredMeetings();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFiltered() {
        return filter.type != NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPlaceAvailableAt(String place, long time, int duration) {
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(time);
        endTime.add(Calendar.MINUTE, duration - 1);

        List<Meeting> ml = getMeetings(place);
        for (Meeting m : ml ) {
            if (time <= m.getEndTime() && endTime.getTimeInMillis() > m.getTime()) return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return filteredMeetings.size();
    }

    private List<Meeting> getMeetings(Long startDate, Long endDate) {
        // Returns a list of meetings filtered by a date range
        List<Meeting> filteredList = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.getTime() >= startDate && m.getTime() < endDate) {
                filteredList.add(m);
            }
        }
        return filteredList;
    }

    private List<Meeting> getMeetings(String place) {
        // Returns a list of meetings filtered by place
        List<Meeting> filteredList = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.getPlace().equals(place)) {
                filteredList.add(m);
            }
        }
        return filteredList;
    }

    private void refreshFilteredMeetings() {
        if (filter.type == NONE) {
            filteredMeetings = meetings;
        } else if (filter.type == DATE) {
            filteredMeetings = getMeetings(filter.startDate, filter.endDate);
        } else if (filter.type == PLACE) {
            filteredMeetings = getMeetings(filter.place);
        }
    }

    private static class Filter {
        private final int type;
        private final Long startDate, endDate;
        private final String place;

        public Filter() {
            type = NONE;
            startDate = null;
            endDate = null;
            place = null;
        }

        public Filter(Long date) {
            type = DATE;
            startDate = date;
            endDate = date + 86400000L;
            place = null;
        }

        public Filter(Long startDate, Long endDate) {
            type = DATE;
            this.startDate = startDate;
            this.endDate = endDate;
            place = null;
        }

        public Filter(String place) {
            type = PLACE;
            this.place = place;
            startDate = null;
            endDate = null;
        }
    }

}
