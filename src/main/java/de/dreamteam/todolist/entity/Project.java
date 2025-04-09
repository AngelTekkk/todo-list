package de.dreamteam.todolist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Name des Projekts

    @Column(nullable = false)
    private String description; // Beschreibung des Projekts

    // Ein Projekt kann viele Aufgaben enthalten
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDo> toDos = new ArrayList<>();
}
