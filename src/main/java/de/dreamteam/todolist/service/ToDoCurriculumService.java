package de.dreamteam.todolist.service;

import de.dreamteam.todolist.entity.Curriculum;
import de.dreamteam.todolist.entity.ToDo;
import de.dreamteam.todolist.entity.ToDoCurriculum;
import de.dreamteam.todolist.repository.CurriculumRepository;
import de.dreamteam.todolist.repository.ToDoCurriculumRepository;
import de.dreamteam.todolist.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoCurriculumService {

    private final ToDoCurriculumRepository toDoCurriculumRepository;
    private final CurriculumRepository curriculumRepository;
    private final ToDoRepository toDoRepository;

    /**
     * Добавляет задачу (ToDo) в учебный план (Curriculum) с указанными датами.
     */
    public ToDoCurriculum addToDoToCurriculum(Long curriculumId, Long toDoId, LocalDate startDate, LocalDate endDate) {
        // Получаем Curriculum; если не найден, выбрасываем исключение
        Curriculum curriculum = curriculumRepository.findById(curriculumId)
                .orElseThrow(() -> new RuntimeException("Curriculum not found with id: " + curriculumId));

        // Получаем ToDo; если не найден, выбрасываем исключение
        ToDo toDo = toDoRepository.findById(toDoId)
                .orElseThrow(() -> new RuntimeException("ToDo not found with id: " + toDoId));

        // Создаём ассоциацию между Curriculum и ToDo
        ToDoCurriculum association = ToDoCurriculum.builder()
                .curriculum(curriculum)
                .toDo(toDo)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // Обновляем двусторонние связи (если это необходимо для логики приложения)
        curriculum.getToDoCurriculumList().add(association);
        toDo.getToDoCurriculumList().add(association);

        // Сохраняем ассоциацию
        return toDoCurriculumRepository.save(association);
    }

    public ToDoCurriculum updateToDoCurriculumDates(Long curriculumId, Long toDoId, LocalDate newStartDate, LocalDate newEnDate) {
        ToDoCurriculum association = toDoCurriculumRepository
                .findByCurriculum_IdAndToDo_Id(curriculumId, toDoId)
                .orElseThrow(() -> new RuntimeException("Association not found for curriculum id " + curriculumId + " and todo id " + toDoId));

        association.setStartDate(newStartDate);
        association.setEndDate(newEnDate);

        return toDoCurriculumRepository.save(association);
    }

    public void removeToDoFromCurriculum(Long curriculumId, Long toDoId) {
        Optional<ToDoCurriculum> associationOpt = toDoCurriculumRepository
                .findByCurriculum_IdAndToDo_Id(curriculumId, toDoId);
        if (associationOpt.isPresent()) {
            toDoCurriculumRepository.delete(associationOpt.get());
        } else {
            throw new RuntimeException("Association not found for curriculum id " + curriculumId + " and todo id " + toDoId);
        }
    }
}

