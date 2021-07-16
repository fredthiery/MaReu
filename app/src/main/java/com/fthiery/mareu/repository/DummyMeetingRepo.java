package com.fthiery.mareu.repository;

import com.fthiery.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyMeetingRepo {
    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(0,"Salle Peach","Réunion d'information",
                    Arrays.asList("jean.talus@lamzone.com","paul.hochon@lamzone.fr","marie.golote@lamzone.fr")),
            new Meeting(60,"Salle Mario", "Brainstorming",
                    Arrays.asList("pierre.hoquet@lamzone.com","patrick.ortrite@lamzone.com","rosalie.mentation@lamzone.com")),
            new Meeting( 256,"Salle Wario","Event Storming",
                    Arrays.asList("sonia.enbloc@lamzone.com","martin.tamarre@lamzone.com"))
    );

    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
