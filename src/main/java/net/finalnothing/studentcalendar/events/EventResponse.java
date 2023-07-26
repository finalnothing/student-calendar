package net.finalnothing.studentcalendar.events;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import net.finalnothing.studentcalendar.model.Event;
import net.finalnothing.studentcalendar.model.Task;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class EventResponse {
    private Long id;
    private CourseInEventResponse course;
    private LocalDate occursOn;
    private LocalDate originalOccursOn;
    private Boolean changed;
    private String name;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String location;
    private List<TaskInEventResponse> tasks;

    public EventResponse(Event event) {
        id = event.getId();
        course = new CourseInEventResponse(event.getCourse());
        occursOn = event.getOccursOn();
        originalOccursOn = event.getOriginalOccursOn();
        changed = event.isChanged();
        name = event.getName();
        fromTime = event.getFromTime();
        toTime = event.getToTime();
        location = event.getLocation();
        tasks = event.getTasks().stream().map(TaskInEventResponse::new).toList();
    }
}
