package es.jlrn.persistence.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
//
    boolean existsByEmail(String email);
    boolean findByEmail(String email);
}
