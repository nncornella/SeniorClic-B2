package es.jlrn.presentation.dto.role;

import java.util.Set;

import es.jlrn.persistence.model.PermissionsEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
//
    private Long id; // El ID puede ser null si es un rol nuevo, no requiere validación.

    @NotBlank(message = "El nombre del rol no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres.")
    //@Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "El nombre del rol solo puede contener letras, números y espacios.")
    private String name;

    @NotNull(message = "La lista de permisos no puede ser nula.")
    @Size(min = 1, message = "Debe incluir al menos un permiso.")
    private Set<@NotNull(message = "El permiso no puede ser nulo.") 
    PermissionsEntity> permissions = new HashSet<>();

    // private Long id; // No se valida, ya que puede ser null al crear un nuevo rol

    // @NotBlank(message = "El nombre del rol no puede estar vacío.")
    // @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres.")
    // private String name;

    // @NotNull(message = "La lista de permisos no puede ser nula.")
    // @Size(min = 1, message = "Debe incluir al menos un permiso.")
    // private Set<@NotNull(message = "El permiso no puede ser nulo.") 
    // PermissionsEntity> permissions = new HashSet<>();
}
    
