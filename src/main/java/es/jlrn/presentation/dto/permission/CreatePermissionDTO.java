package es.jlrn.presentation.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionDTO {
//    
    @NotBlank(message = "El nombre del permiso no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre del permiso debe tener entre 3 y 50 caracteres.")
    private String name;
}