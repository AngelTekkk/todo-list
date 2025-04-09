package de.dreamteam.todolist.service;

import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    // Constructor
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Ein neues Projekt erstellen
    public Project createProject(Project project) {
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
    public Optional<Project> updateProject(Long id, Project projectDetails) {
        return projectRepository.findById(id).map(existingProject -> {
            existingProject.setTitle(projectDetails.getTitle());
            existingProject.setDescription(projectDetails.getDescription());
            // Если требуется обновлять список задач, добавить логику здесь
            return projectRepository.save(existingProject);
        });
    }

    // Projekt nach ID löschen
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}

