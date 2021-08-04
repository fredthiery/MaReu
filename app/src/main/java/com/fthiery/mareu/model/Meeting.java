package com.fthiery.mareu.model;

import java.util.List;
import java.util.Objects;

public class Meeting {

    /**
     * Time and date
     */
    private long time;

    /**
     * Place
     */
    private String place;

    /**
     * Subject of the meeting
     */
    private String title;

    /**
     * A list of participants
     */
    private List<String> participants;

    /**
     * Constructor
     *
     * @param time         Date and time of the meeting, expressed in milliseconds since epoch
     * @param place        Place where the meeting happens
     * @param title        Title of the meeting
     * @param participants Email addresses of the participants
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(time, meeting.time) && Objects.equals(place, meeting.place) && Objects.equals(title, meeting.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, place, title);
    }
}
