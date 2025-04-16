package es.jlrn.presentation.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.jlrn.persistence.model.RoleEntity;
import es.jlrn.presentation.dto.role.CreateRoleDTO;
import es.jlrn.services.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
//
    private final IRoleService roleService;

    @GetMapping("/find-by-name/{roleName}")
    public ResponseEntity<RoleEntity> getRoleByName(@PathVariable String roleName) {
    //    
        try {
            RoleEntity role = roleService.getRoleByName(roleName);
            return ResponseEntity.status(HttpStatus.OK).body(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<RoleEntity> getRoleById(@PathVariable Long id) {
    //    
        try {
            RoleEntity role = roleService.getRoleById(id);
            return ResponseEntity.status(HttpStatus.OK).body(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<RoleEntity>> getAllRoles() {
        List<RoleEntity> roles = roleService.getAllRoles();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<RoleEntity> createRole(@RequestBody CreateRoleDTO dto) {
    // 
        try {
            RoleEntity role = roleService.createRole(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(role); // Devolver 201 si el rol fue creado
        } catch (IllegalArgumentException ex) {
            // Capturar excepciones de negocio (como un rol ya existente)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            // Capturar excepciones generales (por ejemplo, errores de base de datos)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/{roleId}/updatePermisos")
    public ResponseEntity<RoleEntity> updateRolePermissions(@PathVariable Long roleId, @RequestBody Set<String> permissionNames) {
    //    
        try {
            RoleEntity updatedRole = roleService.updateRolePermissions(roleId, permissionNames);
            return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}