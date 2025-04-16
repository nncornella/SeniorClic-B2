package es.jlrn.persistence.repositories.temas;


import es.jlrn.persistence.model.temas.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningLevelRepository extends JpaRepository<LevelEntity, Long>{

}
