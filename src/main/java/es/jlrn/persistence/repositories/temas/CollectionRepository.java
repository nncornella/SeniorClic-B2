package es.jlrn.persistence.repositories.temas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.temas.Collection;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long>{
//
   
}
