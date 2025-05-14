package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.controller.payload.NewProjectPayload;
import de.dreamteam.todolist.controller.payload.UpdateProjectPayload;
import de.dreamteam.todolist.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
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
        List<Project> projects = projectService.getAllProjectsForCurrentUser();

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
    public ResponseEntity<?> getProjectById(
            @PathVariable Long id,
            Locale locale
    ) {
        try {
            Project project = projectService.getProjectByIdForCurrentUser(id);

            UpdateProjectPayload dto = UpdateProjectPayload.builder()
                    .id(project.getId())
                    .title(project.getTitle())
                    .description(project.getDescription())
                    .build();

            String successMessage = messageSource.getMessage(
                    "project.getById.success",
                    new Object[]{project.getId()},
                    locale
            );

            return ResponseEntity.ok(Map.of(
                    "project", dto,
                    "message", successMessage
            ));
        }
        catch (EntityNotFoundException ex) {
            // 404 Not Found
            String notFoundMsg = messageSource.getMessage(
                    "project.not.found",
                    new Object[]{id},
                    locale
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", notFoundMsg));
        }
        catch (AccessDeniedException ex) {
            // 403 Forbidden
            String deniedMsg = messageSource.getMessage(
                    "project.access.denied",
                    null,
                    locale
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", deniedMsg));
        }
    }

    // Aktualisierung des Projekts
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectPayload updatePayload,
            Locale locale
    ) {
        try {
            Project updated = projectService.updateProjectForCurrentUser(id, updatePayload);

            UpdateProjectPayload dto = UpdateProjectPayload.builder()
                    .id(updated.getId())
                    .title(updated.getTitle())
                    .description(updated.getDescription())
                    .build();

            String msg = messageSource.getMessage("project.update.success", null, locale);
            return ResponseEntity.ok(Map.of(
                    "project", dto,
                    "message", msg
            ));
        }
        catch (EntityNotFoundException ex) {
            String notFound = messageSource.getMessage("project.not.found", new Object[]{id}, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", notFound));
        }
        catch (AccessDeniedException ex) {
            String denied = messageSource.getMessage("project.access.denied", null, locale);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", denied));
        }
    }

    @PostMapping("/{projectId}/invite/{userId}")
    public ResponseEntity<Map<String, String>> inviteUser(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Locale locale
    ) {
        try {
            projectService.inviteUser(projectId, userId);
            // Предполагаем, что в messages.properties есть ключ:
            // project.invite.success=Пользователь с ID={1} приглашён в проект {0}
            String message = messageSource.getMessage(
                    "project.invite.success",
                    new Object[]{ projectId, userId },
                    locale
            );
            return ResponseEntity.ok(Map.of("message", message));
        } catch (EntityNotFoundException ex) {
            // если проект или пользователь не найден
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    // Löschung eines Projekts
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(
            @PathVariable Long id,
            Locale locale
    ) {
        try {
            projectService.deleteProjectForCurrentUser(id);
            String msg = messageSource.getMessage("project.delete.success", new Object[]{id}, locale);
            return ResponseEntity.ok(Map.of("message", msg));
        }
        catch (EntityNotFoundException ex) {
            String notFound = messageSource.getMessage("project.not.found", new Object[]{id}, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", notFound));
        }
        catch (AccessDeniedException ex) {
            String denied = messageSource.getMessage("project.access.denied", null, locale);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", denied));
        }
    }
}



