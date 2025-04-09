package de.dreamteam.todolist.entity;

import de.dreamteam.todolist.model.ToDoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "todo")
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ToDoStatus status;

    // TODO Bitte den nachfolgenden Haufen Scheiße auf Richtigkeit prüfen

//    @ManyToOne
//    @JoinColumn(name = "project_id")
//    private Project project = new Project();
//
//    @ManyToMany
//    @JoinTable(
//            name = "curriculum_todo",
//            joinColumns = @JoinColumn(name = "toDo_id"),
//            inverseJoinColumns = @JoinColumn(name = "curriculum_id")
//    )
//    private List<Curriculum> curriculumList = new ArrayList<>();
//
//
//    @ManyToMany
//    private List<User> userList = new ArrayList<>();
}