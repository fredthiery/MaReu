package com.fthiery.mareu;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.repository.DummyMeetingRepo;
import com.fthiery.mareu.service.DummyMeetingList;
import com.fthiery.mareu.service.MeetingList;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeetingListTest {

    private MeetingList meetingList;

    @Before
    public void setup() {
        meetingList = new DummyMeetingList();
    }

    @Test
    public void getMeetingsWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = DummyMeetingRepo.DUMMY_MEETINGS;
        // When
        List<Meeting> meetings = meetingList.getMeetings();
        // Then
        assertThat(meetings,containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void resetFilterWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = DummyMeetingRepo.DUMMY_MEETINGS;
        // When
        meetingList.setFilter();
        // Then
        assertThat(meetingList.getMeetings(),containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void setFilterByDateWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = Arrays.asList(
                new Meeting(1627466424998L,"Salle Mario", "Brainstorming",
                        Arrays.asList("pierre.hoquet@lamzone.com","patrick.ortrite@lamzone.com","rosalie.mentation@lamzone.com")),
                new Meeting(1627466424998L,"Salle Bowser","Briefing",
                        Arrays.asList("marie.golote@lamzone.fr","patrick.ortreat@lamzone.com"))
        );
        // When
        meetingList.setFilter(1627423247312L,1627509658118L);
        // Then
        assertThat(meetingList.getMeetings(),containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void setFilterByPlaceWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = Arrays.asList(
                new Meeting(1627466424998L, "Salle Mario", "Brainstorming",
                        Arrays.asList("pierre.hoquet@lamzone.com", "patrick.ortrite@lamzone.com", "rosalie.mentation@lamzone.com"))
        );
        // When
        meetingList.setFilter("Salle Mario");
        // Then
        assertThat(meetingList.getMeetings(),containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        // Given
        Meeting meetingToDelete = meetingList.getMeetings().get(0);
        // When
        meetingList.deleteMeeting(meetingToDelete);
        // Then
        assertFalse(meetingList.getMeetings().contains(meetingToDelete));
    }

    @Test
    public void addMeetingWithSuccess() {
        // Given
        Meeting meetingToAdd = new Meeting(1627509658118L, "Salle Kamek", "Débriefing",
                Arrays.asList("pierre.hoquet@lamzone.com", "patrick.ortrite@lamzone.com", "rosalie.mentation@lamzone.com"));
        // When
        meetingList.addMeeting(meetingToAdd);
        // Then
        assertTrue(meetingList.getMeetings().contains(meetingToAdd));
    }
}
