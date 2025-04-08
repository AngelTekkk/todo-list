package de.dreamteam.todolist.service;


import de.dreamteam.todolist.controller.payload.NewToDoPayload;
import de.dreamteam.todolist.entity.ToDo;
import de.dreamteam.todolist.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    public ToDo createToDo(NewToDoPayload newToDoPayload) {
        ToDo toDo = new ToDo();
        toDo.setTitle(newToDoPayload.title());
        toDo.setDescription(newToDoPayload.description());
        toDo.setEndDate(newToDoPayload.endDate());
        toDo.setStartDate(newToDoPayload.startDate());
        toDo.setStatus(newToDoPayload.status());
        return toDoRepository.save(toDo);
    }

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public ToDo editToDo(NewToDoPayload editToDo, ToDo toDoToEdit) {
        toDoToEdit.setTitle(editToDo.title());
        toDoToEdit.setDescription(editToDo.description());
        toDoToEdit.setEndDate(editToDo.endDate());
        toDoToEdit.setStartDate(editToDo.startDate());
        toDoToEdit.setStatus(editToDo.status());
        return toDoRepository.save(toDoToEdit);
    }


}
