package net.finalnothing.studentcalendar.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "name is required")
    private String name;

    private String color;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;

    private String weekday;

    private LocalTime fromTime;

    private LocalTime toTime;

    private LocalDate validUntil;

    private String location;
}
