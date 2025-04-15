package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.controller.payload.NewProjectPayload;
import de.dreamteam.todolist.controller.payload.UpdateProjectPayload;
import de.dreamteam.todolist.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo-list-api/projects")
public class ProjectRestController {

    private final ProjectService projectService;
    private final MessageSource messageSource;

    // Ein neues Projekt erstellen
    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody NewProjectPayload newPayload, Locale locale) {
        // Erstellen des Projekts
        Project createdProject = projectService.createProject(newPayload);
        UpdateProjectPayload updatePayload = UpdateProjectPayload.builder()
                .id(createdProject.getId())
                .title(createdProject.getTitle())
                .description(createdProject.getDescription())
                .build();

        // Nachricht über die erfolgreiche Löschung
        String successMessage = messageSource.getMessage("project.creation.success", null, locale);

        // Generierung einer Antwort: Projektdaten und Nachricht
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("project", updatePayload);
        responseMap.put("message", successMessage);

        // Zurücksetzen des Status auf 201 Erstellt
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
    }

    // Abrufen einer Liste aller Projekte
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProjects(Locale locale) {
        // Abrufen der Liste der Projekteinheiten vom Service
        List<Project> projects = projectService.getAllProjects();

        // Umwandlung der Entitätsliste in eine DTO ProjectResponse-Liste
        List<UpdateProjectPayload> responseList = projects.stream()
                .map(project -> UpdateProjectPayload.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .description(project.getDescription())
                        .build())
                .collect(Collectors.toList());

        String messageKey = projects.isEmpty() ? "project.getAll.empty" : "project.getAll.success";
        String message = messageSource.getMessage(messageKey, null, locale);

        // Erstellen Sie eine Antwortkarte, die sowohl eine Liste von Projekten als auch eine Nachricht enthält
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("projects", responseList);
        responseMap.put("message", message);

        // Status 200 OK und Nachricht
        return ResponseEntity.ok(responseMap);
    }

    // Abrufen eines Projekts nach ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id, Locale locale) {
        Optional<Project> projectOpt = projectService.getProjectById(id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            UpdateProjectPayload response = UpdateProjectPayload.builder()
                    .id(project.getId())
                    .title(project.getTitle())
                    .description(project.getDescription())
                    .build();

            // Fügen Sie die Projekt-ID in die Nachricht ein
            String successMessage = messageSource.getMessage("project.getById.success", new Object[]{project.getId()}, locale);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("project", response);
            responseMap.put("message", successMessage);

            // Status 200 OK und Nachricht
            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Aktualisierung des Projekts
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id,
                                           @Valid @RequestBody UpdateProjectPayload updatePayload,
                                           Locale locale) {
        Optional<Project> updatedProjectOpt = projectService.updateProject(id, updatePayload);
        if (updatedProjectOpt.isPresent()) {
            Project updatedProject = updatedProjectOpt.get();
            UpdateProjectPayload response  = UpdateProjectPayload.builder()
                    .id(updatedProject.getId())
                    .title(updatedProject.getTitle())
                    .description(updatedProject.getDescription())
                    .build();

            // Erhalten Sie eine lokalisierte Nachricht über die erfolgreiche Aktualisierung
            String successMessage = messageSource.getMessage("project.update.success", null, locale);

            // Antwort, die das aktualisierte Objekt und eine Nachricht enthält
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("project", response);
            responseMap.put("message", successMessage);

            // Status 200 OK zur Aktualisierung
            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Löschung eines Projekts
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProject(@PathVariable Long id, Locale locale) {
        // Prüfen, ob das Projekt mit der angegebenen ID existiert
        Optional<Project> projectOptional = projectService.getProjectById(id);
        if (projectOptional.isEmpty()) {
            String notFoundMessage = messageSource.getMessage("project.not.found", new Object[]{id}, locale);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", notFoundMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }

        // Wenn das Projekt gefunden hat, dann löschen es
        projectService.deleteProject(id);

        // Erhalten Sie eine lokalisierte Nachricht über die erfolgreiche Löschung
        String successMessage = messageSource.getMessage("project.delete.success", new Object[]{id}, locale);

        // Nachricht
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", successMessage);

        // Status 200 OK und Nachricht
        return ResponseEntity.ok(responseMap);
    }
}



