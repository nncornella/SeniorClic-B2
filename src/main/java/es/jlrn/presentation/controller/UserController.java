package es.jlrn.presentation.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.jlrn.presentation.dto.user.CreateUserDTO;
import es.jlrn.presentation.dto.user.InactiveUserDTO;
import es.jlrn.presentation.dto.user.RegisterUserDTO;
import es.jlrn.presentation.dto.user.UpdateUserDTO;
import es.jlrn.presentation.dto.user.UserEntityDTO;
import es.jlrn.services.interfaces.IUserEntityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {
//
    private final IUserEntityService userService;

    // Método para obtener el usuario por ID
    @GetMapping("/find/{id}")
    public ResponseEntity<UserEntityDTO> getUserById(@PathVariable Long id) {
        Optional<UserEntityDTO> userDTO = userService.getUserId(id);
        return userDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/findUsername/{username}")
    public ResponseEntity<Optional<UserEntityDTO>> getUserName(@PathVariable String username) {
        Optional<UserEntityDTO> userDTO = userService.getUserName(username);
        return userDTO.isPresent()
                ? ResponseEntity.ok(userDTO)
                : ResponseEntity.notFound().build(); // Si no se encuentra, devuelve 404
    }

    @GetMapping("/findActive")
    public ResponseEntity<List<UserEntityDTO>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/findInactive")
    public ResponseEntity<List<InactiveUserDTO>> getInactiveUsers() {
        return ResponseEntity.ok(userService.getInactiveUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntityDTO> createRoleUser(@RequestBody @Valid RegisterUserDTO dto) {
        UserEntityDTO createdUser = userService.registerRoleUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // @PreAuthorize("hasRole('ADMIN')") // solo usuarios con rol ADMIN
    @PostMapping("/create")
    public ResponseEntity<UserEntityDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        UserEntityDTO createdUser = userService.createUser(dto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Endpoint para actualizar los campos de un usuario
    @PutMapping("/updateField/{userId}")
    public ResponseEntity<UserEntityDTO> updateUserFields(@PathVariable Long userId, // Obtener el userId de la URL
            @RequestBody UpdateUserDTO updateDTO) { // Obtener el cuerpo de la solicitud (DTO)

        try {
            // Llamar al servicio para actualizar los campos del usuario
            UserEntityDTO updatedUser = userService.updateUserFields(userId, updateDTO);

            // Devolver la respuesta con el código 200 OK y el DTO del usuario actualizado
            return ResponseEntity.ok(updatedUser);

        } catch (IllegalArgumentException e) {
            // Manejar la excepción de un nombre de usuario ya existente o la actualización
            // de la persona
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            // Manejar la excepción si no se encuentra el usuario o la persona
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Manejar cualquier otro tipo de excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserEntityDTO> updateUser(@Valid @PathVariable Long id, @RequestBody CreateUserDTO dto) {
        UserEntityDTO updatedUser = userService.updateUser(dto, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateUser(@PathVariable Long id) {
        try {
            userService.reactivateUser(id);
            return ResponseEntity.ok().build(); // Devuelve un estado 200 OK si todo fue bien
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found si el usuario no existe
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request si el usuario ya está
                                                                          // activo
        }
    }    

    // Endpoint para desactivar un usuario
    @PutMapping("/{userId}/desactivate")
    public ResponseEntity<Void> desactivateUser(@PathVariable Long userId) {
        try {
            userService.desactivateUser(userId);
            return ResponseEntity.ok().build(); // Devuelve un estado 200 OK si todo fue bien
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found si el usuario no existe
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request si el usuario ya está
                                                                          // activo
        }
    }

    @PutMapping("/{userId}/username")
    public ResponseEntity<Void> changeUsername(@PathVariable Long userId, @RequestParam String newUsername) {
        try {
            userService.changeUsername(userId, newUsername);
            return ResponseEntity.ok().build(); // Devuelve un estado 200 OK si el cambio fue exitoso
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found si el usuario no existe
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request si el nombre de usuario ya
                                                                          // está en uso
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long userId, @RequestParam String newPassword) {
        try {
            userService.changePassword(userId, newPassword);
            return ResponseEntity.ok().build(); // Devuelve un estado 200 OK si el cambio fue exitoso
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found si el usuario no existe
        }
    }

    // Cambiar el rol de un usuario (Agregar un nuevo rol)
    @PatchMapping("/{userId}/addRole")
    public ResponseEntity<String> addUserRole(@PathVariable Long userId, @RequestBody Map<String, String> roleRequest) {
    //
        // Obtener el nuevo rol desde el JSON
        String newRoleName = roleRequest.get("newRoleName");

        try {
            // Llamamos al servicio para cambiar el rol del usuario
            userService.addUserRole(userId, newRoleName);
            return ResponseEntity.ok("Rol actualizado correctamente.");
        } catch (EntityNotFoundException | IllegalArgumentException | IllegalStateException ex) {
            // En caso de error, devolvemos un BAD_REQUEST con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }    
    }

    // Cambiar o agregar un nuevo rol a un usuario
    @PostMapping("/{userId}/role")
    public ResponseEntity<String> changeUserRole(
            @PathVariable Long userId, 
            @RequestBody Map<String, String> roleRequest) {

        // Obtener el nombre del nuevo rol desde el JSON
        String newRoleName = roleRequest.get("newRoleName");

        try {
            // Llamamos al servicio para cambiar el rol del usuario
            userService.changeUserRole(userId, newRoleName);
            return ResponseEntity.ok("Rol añadido correctamente.");
        } catch (EntityNotFoundException ex) {
            // Si no se encuentra el usuario o el rol
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            // Si el rol no tiene permisos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (IllegalStateException ex) {
            // Si el usuario ya tiene asignado el rol
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // Eliminar un rol de un usuario
    @DeleteMapping("/{userId}/role")
    public ResponseEntity<String> removeUserRole(@PathVariable Long userId, @RequestBody Map<String, String> roleRequest) {
    //
        // Obtener el rol a eliminar desde el JSON
        String roleToRemove = roleRequest.get("roleToRemove");

        try {
            // Llamamos al servicio para eliminar el rol del usuario
            userService.removeUserRole(userId, roleToRemove);
            return ResponseEntity.ok("Rol eliminado correctamente.");
        } catch (EntityNotFoundException | IllegalArgumentException | IllegalStateException ex) {
            // En caso de error, devolvemos un BAD_REQUEST con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
