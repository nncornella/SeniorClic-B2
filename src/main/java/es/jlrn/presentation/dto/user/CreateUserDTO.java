package es.jlrn.presentation.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
//
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres.")
    //@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    //@Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}", 
    //        message = "La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula, un número y un carácter especial.")
    private String password;

    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    private String email;

    @NotNull(message = "El ID de la persona es obligatorio.")
    private Long personID;  // Cambié a Long para permitir que sea null si es necesario

    @NotBlank(message = "El nombre del rol es obligatorio.")
    @Size(max = 50, message = "El nombre del rol no puede exceder los 50 caracteres.")
    private String roleName;

    @Builder.Default
    private boolean activo = true;

    // @NotBlank(message = "El nombre de usuario es obligatorio")
    // private String username;

    // @NotBlank(message = "La contraseña es obligatoria")
    // @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    // private String password;

    // @NotNull(message = "El ID de la persona es obligatorio")
    // private long personID;

    // @NotBlank(message = "El nombre del rol es obligatorio")
    // private String roleName;

    // @Builder.Default
    // private boolean activo = true;
}
