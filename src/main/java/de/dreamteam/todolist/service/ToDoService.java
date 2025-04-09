package de.dreamteam.todolist.service;


import de.dreamteam.todolist.controller.payload.NewToDoPayload;
import de.dreamteam.todolist.controller.payload.UpdateToDoPayload;
import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.entity.ToDo;
import de.dreamteam.todolist.repository.ProjectRepository;
import de.dreamteam.todolist.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ToDo createToDo(NewToDoPayload payload) {
        ToDo toDo = ToDo.builder()
                .title(payload.title())
                .description(payload.description())
                .endDate(payload.endDate())
                .startDate(payload.startDate())
                .status(payload.status())
                .project(payload.project())
                .toDoCurriculumList(payload.toDoCurriculumList())
                .userList(payload.userList())
                .build();
        if (payload.project() != null) {
            Project project = projectRepository.findById(payload.project().getId()).orElseThrow();
            project.getToDos().add(toDo);
        }
        return toDoRepository.save(toDo);
    }

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public ToDo updateToDo(UpdateToDoPayload payload, ToDo existingToDo) {
        existingToDo.setTitle(payload.title());
        existingToDo.setDescription(payload.description());
        existingToDo.setEndDate(payload.endDate());
        existingToDo.setStartDate(payload.startDate());
        existingToDo.setStatus(payload.status());
        return toDoRepository.save(existingToDo);
    }
}
