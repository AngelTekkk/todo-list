package de.dreamteam.todolist.service;

import de.dreamteam.todolist.controller.payload.NewProjectPayload;
import de.dreamteam.todolist.controller.payload.UpdateProjectPayload;
import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    // Ein neues Projekt erstellen
    public Project createProject(NewProjectPayload newPayload) {
        Project project = Project.builder()
                .title(newPayload.title())
                .description(newPayload.description())
                .build();

        return projectRepository.save(project);
    }

    // Eine Liste aller Projekte erhalten
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Projekt nach ID abrufen
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    // Projektdaten nach ID aktualisieren
    public Optional<Project> updateProject(Long id, UpdateProjectPayload updatePayload) {
        return projectRepository.findById(id).map(existingProject -> {
            // Hier findet die Zuordnung von DTO zu Entity statt:
            existingProject.setTitle(updatePayload.title());
            existingProject.setDescription(updatePayload.description());
            return projectRepository.save(existingProject);
        });
    }

    // Projekt nach ID löschen
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

}

