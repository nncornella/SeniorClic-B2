package es.jlrn.presentation.dto.role;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleDTO {
//
    @NotBlank(message = "El nombre del rol no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres.")
    //@Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "El nombre del rol solo puede contener letras, números y espacios.")
    private String name;

    @NotEmpty(message = "Debe proporcionar al menos un permiso.")
    private Set<@NotBlank(message = "El nombre del permiso no puede estar vacío.")
                @Size(max = 50, message = "El nombre del permiso no puede exceder los 50 caracteres.")
                //@Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "El nombre del permiso solo puede contener letras, números y espacios.") 
                String> permissionNames;

    // @NotBlank(message = "El nombre del rol no puede estar vacío.")
    // @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres.")
    // private String name;

    // @NotEmpty(message = "Debe proporcionar al menos un permiso.")
    // private Set<@NotBlank(message = "El nombre del permiso no puede estar vacío.") 
    // String> permissionNames;

}