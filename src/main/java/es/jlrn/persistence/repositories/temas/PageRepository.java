package es.jlrn.persistence.repositories.temas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.temas.Page;

@Repository 
public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> findBySubtopicId(Long subtopicId);
}