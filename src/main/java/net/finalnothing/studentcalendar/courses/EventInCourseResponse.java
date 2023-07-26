package net.finalnothing.studentcalendar.courses;

import lombok.Data;
import net.finalnothing.studentcalendar.model.Event;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventInCourseResponse {
    private Long id;
    private LocalDate occursOn;
    private LocalDate originalOccursOn;
    private Boolean changed;
    private String name;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String location;

    public EventInCourseResponse(Event event) {
        id = event.getId();
        occursOn = event.getOccursOn();
        originalOccursOn = event.getOriginalOccursOn();
        changed = event.isChanged();
        name = event.getName();
        fromTime = event.getFromTime();
        toTime = event.getToTime();
        location = event.getLocation();
    }
}
