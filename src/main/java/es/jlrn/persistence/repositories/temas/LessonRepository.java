package es.jlrn.persistence.repositories.temas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.temas.Lesson;

@Repository 
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByBookId(Long bookId);
}