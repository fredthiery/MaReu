package com.fthiery.mareu.repository;

import com.fthiery.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyMeetingRepo {
    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(1628505023687L,"Salle Mario","Réunion d'information",
                    Arrays.asList("jean.talus@lamzone.com","paul.hochon@lamzone.fr","marie.golote@lamzone.fr")),
            new Meeting(1628668823687L,"Salle Luigi", "Brainstorming",
                    Arrays.asList("pierre.hoquet@lamzone.com","patrick.ortrite@lamzone.com","rosalie.mentation@lamzone.com")),
            new Meeting(1628691323687L,"Salle Peach","Event Storming",
                    Arrays.asList("sonia.enbloc@lamzone.com","martin.tamarre@lamzone.com")),
            new Meeting(1628769623687L,"Salle Bowser","Briefing",
                    Arrays.asList("marie.golote@lamzone.fr","patrick.ortreat@lamzone.com"))
    );

    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
