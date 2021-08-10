package com.fthiery.mareu.model;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Meeting {

    /** Time and date in milliseconds */
    private final Calendar dateTime = Calendar.getInstance();

    /** Place */
    private String place;

    /** Subject of the meeting */
    private String title;

    /** A list of participants */
    private List<String> participants;

    /** Duration of the meeting in minutes */
    private int duration;

    /**
     * Constructor
     * @param time  Date and time of the meeting, expressed in milliseconds since epoch
     * @param place  Place where the meeting happens
     * @param title  Title of the meeting
     * @param participants  Email addresses of the participants
     * @param duration  Duration of the meeting in minutes
     */
    public Meeting(long time, String place, String title, List<String> participants, int duration) {
        this.dateTime.setTimeInMillis(time);
        this.place = place;
        this.title = title;
        this.participants = participants;
        this.duration = duration;
    }

    public Meeting(long time, String place, String title, List<String> participants) {
        this(time, place, title, participants, 45);
    }

    public Meeting(long time, String place, String title, String participants, int duration) {
        participants = participants.replaceAll("\\s+", "").replace(",", ", ");
        List<String> p = Arrays.asList(participants.split(", "));
        this.dateTime.setTimeInMillis(time);
        this.place = place;
        this.title = title;
        this.participants = p;
        this.duration = duration;
    }

    public Meeting(long time, String place, String title, String participants) {
        this(time,place,title,participants,45);
    }

    public long getTime() {
        return dateTime.getTimeInMillis();
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public long getEndTime() {
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(dateTime.getTime());
        endTime.add(Calendar.MINUTE, duration);
        return endTime.getTimeInMillis();
    }

    public void setDate(long date) {
        Calendar newDate = Calendar.getInstance(TimeZone.getDefault());
        newDate.setTimeInMillis(date);
        dateTime.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH));
    }

    public void setTime(long time) {
        dateTime.setTimeInMillis(time);
    }

    public void setTime(int hour, int minute) {
        dateTime.set(Calendar.HOUR_OF_DAY, hour);
        dateTime.set(Calendar.MINUTE, minute);
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

    public void setParticipants(String participants) {
        participants = participants.replaceAll("\\s+", "").replace(",", ", ");
        List<String> p = Arrays.asList(participants.split(", "));
        setParticipants(p);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFormattedDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(dateTime.getTime());
    }

    public String getFormattedTime() {
        SimpleDateFormat format = new SimpleDateFormat("H'h'mm", Locale.getDefault());
        return format.format(dateTime.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(dateTime, meeting.dateTime) && Objects.equals(place, meeting.place) && Objects.equals(title, meeting.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime.getTimeInMillis(),place,title);
    }
}
