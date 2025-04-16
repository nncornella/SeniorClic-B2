package es.jlrn.persistence.repositories.temas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.temas.Chapter;

@Repository 
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByLessonId(Long lessonId);
}