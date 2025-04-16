package es.jlrn.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import es.jlrn.persistence.model.PermissionsEntity;
import es.jlrn.persistence.repositories.PermissionsEntityRepository;
import es.jlrn.presentation.dto.permission.CreatePermissionDTO;
import es.jlrn.services.interfaces.IPermissionService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements IPermissionService{
//
    private final PermissionsEntityRepository permissionRepository; 

    // Buscar permiso por ID
    @Override
    @Transactional(readOnly = true)
    public PermissionsEntity getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado con ID: " + id));
    }

    // Buscar permiso por nombre
    @Override
    @Transactional(readOnly = true)
    public PermissionsEntity getPermissionByName(String name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado con nombre: " + name));
    }

    // Obtener todos los permisos
    @Override
    @Transactional(readOnly = true)
    public List<PermissionsEntity> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    @Transactional
    public PermissionsEntity createPermission(CreatePermissionDTO dto) {
    //    
        // Verificar si el nombre del permiso es nulo o vacío
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del permiso no puede estar vacío");
        }

        // Verificar si el permiso ya existe
        if (permissionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("El permiso '" + dto.getName() + "' ya existe");
        }

        // Crear la entidad de Permission
        PermissionsEntity permission = PermissionsEntity.builder()
                .name(dto.getName())
                .build();

        // Guardar el permiso en la base de datos
        return permissionRepository.save(permission);
    }

}


