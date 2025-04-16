package es.jlrn.persistence.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.RoleEntity;

@Repository
public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {
//    
    Optional<RoleEntity> findByName(String name);
    Optional<RoleEntity> findByNameIn(Set<String> name);
    boolean existsByName(String name);
}
