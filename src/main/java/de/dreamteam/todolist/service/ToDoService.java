package de.dreamteam.todolist.service;

import de.dreamteam.todolist.controller.payload.NewToDoPayload;
import de.dreamteam.todolist.controller.payload.UpdateToDoPayload;
import de.dreamteam.todolist.entity.Curriculum;
import de.dreamteam.todolist.entity.Project;
import de.dreamteam.todolist.entity.ToDo;
import de.dreamteam.todolist.entity.ToDoCurriculum;
import de.dreamteam.todolist.repository.CurriculumRepository;
import de.dreamteam.todolist.repository.ProjectRepository;
import de.dreamteam.todolist.repository.ToDoCurriculumRepository;
import de.dreamteam.todolist.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CurriculumRepository curriculumRepository;

    @Autowired
    private ToDoCurriculumRepository toDoCurriculumRepository;

    public void createToDo(NewToDoPayload payload) {
        ToDo toDo = ToDo.builder()
                .title(payload.title())
                .description(payload.description())
                .endDate(payload.endDate())
                .startDate(payload.startDate())
                .status(payload.status())
                .build();

        if (payload.projectId() != null) {
            Project project = projectRepository.findById(payload.projectId()).orElseThrow();
            toDo.setProject(project);
        }

        toDoRepository.save(toDo);
    }

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public void updateToDo(UpdateToDoPayload payload, ToDo existingToDo) {
        existingToDo.setTitle(payload.title());
        existingToDo.setDescription(payload.description());
        existingToDo.setEndDate(payload.endDate());
        existingToDo.setStartDate(payload.startDate());
        existingToDo.setStatus(payload.status());
        if (payload.projectId() != null) {
            Project project = projectRepository.findById(payload.projectId()).orElseThrow();
            existingToDo.setProject(project);
        }
        assignToCurriculum(existingToDo, payload);
        toDoRepository.save(existingToDo);
    }

    public void assignToProject(ToDo toDo, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        project.getToDos().add(toDo);
        toDo.setProject(project);
        toDoRepository.save(toDo);
    }

    public void assignToCurriculum(ToDo existingToDo, UpdateToDoPayload payload) {
        List<ToDoCurriculum> existingToDoCurriculums = toDoCurriculumRepository.findAll();

        if (payload.curriculumIds() != null) {
            List<Curriculum> curriculums = curriculumRepository.findAllById(payload.curriculumIds());
            List<ToDoCurriculum> toDoCurriculumList = new ArrayList<>();

            for (Curriculum curriculum : curriculums) {
                for (ToDoCurriculum existingToDoCurriculum : existingToDoCurriculums) {
                    if (!Objects.equals(existingToDoCurriculum.getCurriculum().getId(), curriculum.getId())
                            && !Objects.equals(existingToDoCurriculum.getToDo().getId(), existingToDo.getId())) {

                        ToDoCurriculum toDoCurriculum = new ToDoCurriculum();
                        toDoCurriculum.setToDo(existingToDo);
                        toDoCurriculum.setCurriculum(curriculum);
                        toDoCurriculumList.add(toDoCurriculum);
                        toDoCurriculumRepository.save(toDoCurriculum);
                    }
                }
            }
            existingToDo.setToDoCurriculumList(toDoCurriculumList);
        } else {
            List<ToDoCurriculum> toDelete = existingToDoCurriculums.stream()
                    .filter(toDoCurriculum -> Objects.equals(toDoCurriculum.getToDo().getId(), existingToDo.getId()))
                    .toList();
            toDoCurriculumRepository.deleteAll(toDelete);
            existingToDo.setToDoCurriculumList(null);
        }
    }
}
