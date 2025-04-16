package es.jlrn.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.jlrn.persistence.model.PermissionsEntity;
import es.jlrn.persistence.model.RoleEntity;
import es.jlrn.persistence.repositories.PermissionsEntityRepository;
import es.jlrn.persistence.repositories.RoleEntityRepository;
import es.jlrn.presentation.dto.role.CreateRoleDTO;
import es.jlrn.services.interfaces.IRoleService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    //
    private final RoleEntityRepository roleRepository;
    private final PermissionsEntityRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public RoleEntity getRoleByName(String roleName) {
    //    
        String normalizedRoleName = normalizeRoleName(roleName);
        return roleRepository.findByName(normalizedRoleName )
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con el nombre: " + roleName));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleEntity getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public RoleEntity createRole(CreateRoleDTO dto) {
        //
        // Verificar que el nombre del rol no esté vacío
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
        }

        String normalizedRoleName = normalizeRoleName(dto.getName());

        // Verifica si ya existe el rol
        roleRepository.findByName(normalizedRoleName).ifPresent(role -> {
            throw new IllegalArgumentException("El rol '" + normalizedRoleName + "' ya existe");
        });

        // Obtener permisos existentes
        Set<PermissionsEntity> foundPermissions = permissionRepository.findByNameIn(dto.getPermissionNames());

        // Verifica si faltan permisos
        Set<String> foundNames = foundPermissions.stream()
                .map(PermissionsEntity::getName)
                .collect(Collectors.toSet());

        Set<String> missing = dto.getPermissionNames().stream()
                .filter(name -> !foundNames.contains(name))
                .collect(Collectors.toSet());

        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Permisos no encontrados: " + String.join(", ", missing));
        }

        // Asegurarse de que haya al menos un permiso asignado
        if (foundPermissions.isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso.");
        }

        // Construir y guardar el rol
        RoleEntity role = RoleEntity.builder()
                .name(normalizedRoleName)
                .permissions(foundPermissions)
                .build();

        RoleEntity savedRole = roleRepository.save(role); // Guardar el rol en la base de datos

        return savedRole;
    }

    @Override
    @Transactional
    public RoleEntity updateRolePermissions(Long roleId, Set<String> newPermissionNames) {
        //
        // Verifica si el rol existe
        RoleEntity existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("El rol con id " + roleId + " no existe"));

        // Obtener los permisos actuales del rol
        Set<PermissionsEntity> currentPermissions = existingRole.getPermissions();

        // Obtener los nuevos permisos de la base de datos
        Set<PermissionsEntity> newPermissions = permissionRepository.findByNameIn(newPermissionNames);

        // Verifica si se han proporcionado nuevos permisos
        if (newPermissions.isEmpty()) {
            throw new IllegalArgumentException("El rol debe tener al menos un permiso.");
        }

        // Verifica si faltan permisos
        Set<String> currentNames = currentPermissions.stream()
                .map(PermissionsEntity::getName)
                .collect(Collectors.toSet());

        Set<String> newNames = newPermissions.stream()
                .map(PermissionsEntity::getName)
                .collect(Collectors.toSet());

        // Determinar los permisos a agregar (nuevos permisos que no están en el rol)
        Set<PermissionsEntity> permissionsToAdd = newPermissions.stream()
                .filter(permission -> !currentNames.contains(permission.getName()))
                .collect(Collectors.toSet());

        // Determinar los permisos a eliminar (permisos que ya no están en la nueva
        // lista)
        Set<PermissionsEntity> permissionsToRemove = currentPermissions.stream()
                .filter(permission -> !newNames.contains(permission.getName()))
                .collect(Collectors.toSet());

        // Agregar los nuevos permisos
        currentPermissions.addAll(permissionsToAdd);

        // Eliminar los permisos que ya no están
        currentPermissions.removeAll(permissionsToRemove);

        // Si después de la actualización, el rol no tiene permisos, lanzar una
        // excepción
        if (currentPermissions.isEmpty()) {
            throw new IllegalArgumentException("El rol no puede quedar sin permisos.");
        }

        // Guardar los cambios en el rol
        RoleEntity savedRole = roleRepository.save(existingRole);

        return savedRole;
    }

    //
    private String normalizeRoleName(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
        }
        return rawName.toUpperCase().startsWith("ROLE_")
                ? rawName.toUpperCase()
                : "ROLE_" + rawName.toUpperCase();
    }

}