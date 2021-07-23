package com.fthiery.mareu;

import com.fthiery.mareu.model.Meeting;
import com.fthiery.mareu.repository.DummyMeetingRepo;
import com.fthiery.mareu.service.DummyMeetingApiService;
import com.fthiery.mareu.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MeetingServiceTest {

    private MeetingApiService service;

    @Before
    public void setup() {
        service = new DummyMeetingApiService();
    }

    @Test
    public void getMeetingsWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = DummyMeetingRepo.DUMMY_MEETINGS;
        // When
        List<Meeting> meetings = service.getMeetings();
        // Then
        assertThat(meetings,containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void getMeetingsFilteredByDateWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = Arrays.asList(
                new Meeting(1627466424998L,"Salle Mario", "Brainstorming",
                        Arrays.asList("pierre.hoquet@lamzone.com","patrick.ortrite@lamzone.com","rosalie.mentation@lamzone.com")),
                new Meeting(1627466424998L,"Salle Bowser","Briefing",
                        Arrays.asList("marie.golote@lamzone.fr","patrick.ortreat@lamzone.com"))
        );
        // When
        List<Meeting> meetings = service.getMeetings(1627423247312L,1627509658118L);
        // Then
        assertThat(meetings,containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void getMeetingsFilteredByPlaceWithSuccess() {
        // Given
        List<Meeting> expectedMeetings = Arrays.asList(
                new Meeting(1627466424998L, "Salle Mario", "Brainstorming",
                        Arrays.asList("pierre.hoquet@lamzone.com", "patrick.ortrite@lamzone.com", "rosalie.mentation@lamzone.com"))
        );
        // When
        List<Meeting> meetings = service.getMeetings("Salle Mario");
        // Then
        assertThat(meetings,containsInAnyOrder(expectedMeetings.toArray()));
    }

    @Test
    public void deleteMeetingWithSuccess() {
        // Given
        Meeting meetingToDelete = service.getMeetings().get(0);
        // When
        service.deleteMeeting(meetingToDelete);
        // Then
        assertFalse(service.getMeetings().contains(meetingToDelete));
    }

    @Test
    public void addMeetingWithSuccess() {
        // Given
        Meeting meetingToAdd = new Meeting(1627509658118L, "Salle Kamek", "Débriefing",
                Arrays.asList("pierre.hoquet@lamzone.com", "patrick.ortrite@lamzone.com", "rosalie.mentation@lamzone.com"));
        // When
        service.addMeeting(meetingToAdd);
        // Then
        assertTrue(service.getMeetings().contains(meetingToAdd));
    }
}
