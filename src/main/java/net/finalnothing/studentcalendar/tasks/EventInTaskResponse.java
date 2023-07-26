package net.finalnothing.studentcalendar.tasks;

import lombok.Data;
import net.finalnothing.studentcalendar.model.Event;

import java.time.LocalDate;

@Data
public class EventInTaskResponse {
    private Long id;
    private Long courseId;
    private LocalDate occursOn;
    private String name;

    public EventInTaskResponse(Event event) {
        id = event.getId();
        courseId = event.getCourse().getId();
        occursOn = event.getOccursOn();
        name = event.getName();
    }
}
