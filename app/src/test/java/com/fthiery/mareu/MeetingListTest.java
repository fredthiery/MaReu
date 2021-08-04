package com.fthiery.mareu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.repository.DummyMeetingRepo;
import com.fthiery.mareu.service.DummyMeetingList;
import com.fthiery.mareu.service.MeetingList;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
                new Meeting(1628668823687L,"Salle Luigi", "Brainstorming",
                        Arrays.asList("pierre.hoquet@lamzone.com","patrick.ortrite@lamzone.com","rosalie.mentation@lamzone.com")),
                new Meeting(1628691323687L,"Salle Peach","Event Storming",
                        Arrays.asList("sonia.enbloc@lamzone.com","martin.tamarre@lamzone.com"))
        );
        // When
        meetingList.setFilter(1628628823687L,1628741323687L);
        // Then
        assertThat(meetingList.getMeetings(),containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void setFilterByPlaceWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = Arrays.asList(
                new Meeting(1628505023687L,"Salle Mario","Réunion d'information",
                        Arrays.asList("jean.talus@lamzone.com","paul.hochon@lamzone.fr","marie.golote@lamzone.fr"))
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
