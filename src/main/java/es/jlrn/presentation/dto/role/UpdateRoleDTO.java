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
public class UpdateRoleDTO {
//
    @NotBlank(message = "El nombre del rol no puede estar vacío.")
    @Size(max = 100, message = "El nombre del rol no puede exceder los 100 caracteres.")
    private String name;

    @NotEmpty(message = "Debe proporcionar al menos un permiso.")
    private Set<@NotBlank(message = "El nombre del permiso no puede estar vacío.") @Size(max = 50, message = "El nombre del permiso no puede exceder los 50 caracteres.") 
    String> permissionNames;

    // @NotBlank(message = "El nombre del rol no puede estar vacío.")
    // private String name;

    // @NotEmpty(message = "Debe proporcionar al menos un permiso.")
    // private Set<@NotBlank(message = "El nombre del permiso no puede estar vacío.") 
    // String> permissionNames;
}
