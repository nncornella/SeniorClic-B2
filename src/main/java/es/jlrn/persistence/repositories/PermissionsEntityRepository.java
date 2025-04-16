package es.jlrn.persistence.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jlrn.persistence.model.PermissionsEntity;

@Repository
public interface PermissionsEntityRepository extends JpaRepository<PermissionsEntity, Long> {
//
    Set<PermissionsEntity> findByNameIn(Set<String> names);
    boolean existsByName(String name);  // Método para verificar si existe un permiso con un nombre
    Optional<PermissionsEntity> findById(Long id);  // Método para encontrar permiso por ID
    Optional<PermissionsEntity> findByName(String name);  // Método para encontrar permiso por nombre
    List<PermissionsEntity> findAll();  // Método para obtener todos los permisos
}
