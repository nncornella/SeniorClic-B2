package es.jlrn.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.jlrn.persistence.model.PermissionsEntity;
import es.jlrn.presentation.dto.permission.CreatePermissionDTO;
import es.jlrn.services.interfaces.IPermissionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permissions")
public class PermissionController {
//
    private final IPermissionService permissionService;

     // Obtener permiso por ID
    @GetMapping("/find/{id}")
    public ResponseEntity<PermissionsEntity> getPermissionById(@PathVariable Long id) {
    //    
        try {
            PermissionsEntity permission = permissionService.getPermissionById(id);
            return ResponseEntity.ok(permission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Obtener permiso por nombre
    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<PermissionsEntity> getPermissionByName(@PathVariable String name) {
    //    
        try {
            PermissionsEntity permission = permissionService.getPermissionByName(name);
            return ResponseEntity.ok(permission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Obtener todos los permisos
    @GetMapping("/findAll")
    public ResponseEntity<List<PermissionsEntity>> getAllPermissions() {
    //    
        List<PermissionsEntity> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PostMapping("/create")
    public ResponseEntity<PermissionsEntity> createPermission(@RequestBody CreatePermissionDTO dto) {
    //    
        try {
            PermissionsEntity permission = permissionService.createPermission(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(permission);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}