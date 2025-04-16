package es.jlrn.services.interfaces;

import java.util.List;

import es.jlrn.persistence.model.PermissionsEntity;
import es.jlrn.presentation.dto.permission.CreatePermissionDTO;

public interface IPermissionService {
//
    PermissionsEntity getPermissionById(Long id);
    PermissionsEntity getPermissionByName(String name);
     List<PermissionsEntity> getAllPermissions();
    PermissionsEntity createPermission(CreatePermissionDTO dto);
}
