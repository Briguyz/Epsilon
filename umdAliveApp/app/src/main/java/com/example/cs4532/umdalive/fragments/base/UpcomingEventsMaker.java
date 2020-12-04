package com.example.cs4532.umdalive.fragments.base;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : Henry Trinh
 * This is the class that will take in Upcoming eventData data
 *
 */
public class UpcomingEventsMaker implements Comparable<UpcomingEventsMaker> {

    String EventName;
    String EventDate;
    String EventTime;
    String EventId;

    Date DateofEvent;
    Date DateofEvent2;

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public UpcomingEventsMaker(String eventName, String eventDate, String eventTime, String id) {
       EventName = eventName;
       EventDate = eventDate;
       EventTime = eventTime;
       EventId = id;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }
    /**
     * @author Henry Trinh
     * @param o object coming in
     * @return int
     *
     * This function will compareTo entries one coming in and the current object
     * Then returns an int depending on the condition
     */
    @Override
    public int compareTo(@NonNull UpcomingEventsMaker o) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            DateofEvent = formatter.parse(getEventDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            DateofEvent2 = formatter.parse(o.getEventDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateofEvent.compareTo(DateofEvent2);
    }
}