package es.jlrn.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.Person;
import es.jlrn.persistence.model.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
//
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAllByActivoTrue();
    List<UserEntity> findAllByActivoFalse(); 
    Optional<UserEntity> findByPerson(Person person);
    boolean existsByEmail(String email);
    boolean existsByPersonId(Long personId);
    boolean existsByUsername(String userName);
}