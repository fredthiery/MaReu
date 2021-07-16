package com.fthiery.mareu.model;

import java.util.List;

public class Meeting {

    /** Time and date */
    private long time;

    /** Place */
    private String place;

    /** Subject of the meeting */
    private String title;

    /** A list of participants */
    private List<String> participants;

    /**
     * Constructor
     * @param time
     * @param place
     * @param title
     * @param participants
     */
    public Meeting(long time, String place, String title, List<String> participants) {
        this.time = time;
        this.place = place;
        this.title = title;
        this.participants = participants;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
