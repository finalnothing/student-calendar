package net.finalnothing.studentcalendar.events;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class UpdateEventRequest implements Serializable {
    private LocalDate occursOn;
    private String name;
    private LocalTime fromTime;
    private LocalTime toTime;
    private String location;
}
