package de.dreamteam.todolist.entity;

import de.dreamteam.todolist.model.ToDoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Eins-zu-Viele-Beziehung zur assoziativen Entität (Aufgabe - Lehrplan)
    @OneToMany(mappedBy = "toDo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDoCurriculum> toDoCurriculumList = new ArrayList<>();

    @ManyToMany
    private List<User> userList = new ArrayList<>();
}