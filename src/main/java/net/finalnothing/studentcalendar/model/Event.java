package net.finalnothing.studentcalendar.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDate occursOn;

    private LocalDate originalOccursOn;

    @ColumnDefault("false")
    private boolean changed;

    private String name;

    private LocalTime fromTime;

    private LocalTime toTime;

    private String location;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Task> tasks = List.of();
}
