package de.dreamteam.todolist.controller;

import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.controller.payload.ProjectRequest;
import de.dreamteam.todolist.controller.payload.ProjectResponse;
import de.dreamteam.todolist.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo-list-api/projects")
public class ProjectController {

    private final ProjectService projectService;

    // Constructor
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Ein neues Projekt erstellen
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        System.out.println("Create project");
        Project project = Project.builder()
                .title(projectRequest.title())
                .description(projectRequest.description())
                .build();

        Project createdProject = projectService.createProject(project);
        ProjectResponse response = ProjectResponse.builder()
                .id(createdProject.getId())
                .title(createdProject.getTitle())
                .description(createdProject.getDescription())
                .build();

        return ResponseEntity.ok(response);
    }

    // Abrufen einer Liste aller Projekte
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectResponse> responseList = projects.stream()
                .map(project -> ProjectResponse.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .description(project.getDescription())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    // Abrufen eines Projekts nach ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(project -> {
                    ProjectResponse response = ProjectResponse.builder()
                            .id(project.getId())
                            .title(project.getTitle())
                            .description(project.getDescription())
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Aktualisierung des Projekts
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest projectRequest) {
        Project projectDetails = Project.builder()
                .title(projectRequest.title())
                .description(projectRequest.description())
                .build();

        return projectService.updateProject(id, projectDetails)
                .map(updatedProject -> {
                    ProjectResponse response = ProjectResponse.builder()
                            .id(updatedProject.getId())
                            .title(updatedProject.getTitle())
                            .description(updatedProject.getDescription())
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Löschung eines Projekts
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}



