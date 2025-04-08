package de.dreamteam.todolist.repository;

import de.dreamteam.todolist.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {

}

