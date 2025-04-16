package es.jlrn.services.interfaces;


import java.util.List;
import java.util.Set;

import es.jlrn.persistence.model.RoleEntity;
import es.jlrn.presentation.dto.role.CreateRoleDTO;

public interface IRoleService {
//
    RoleEntity getRoleByName(String roleName);
    RoleEntity getRoleById(Long id);
    List<RoleEntity> getAllRoles();
    RoleEntity createRole(CreateRoleDTO dto);
    RoleEntity updateRolePermissions(Long roleId, Set<String> newPermissionNames);
}
