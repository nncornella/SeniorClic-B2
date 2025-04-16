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
public class RegisterUserDTO {
//
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    //@Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}",
    //      message = "La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula, un número y un carácter especial.")
    private String password;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    private String email;

    @NotNull(message = "El ID de la persona es obligatorio.")
    private Long personID;  // Se usa Long para permitir valores nulos si es necesario

    @Builder.Default
    private boolean activo = true; // Se asegura que el usuario esté activo por defecto

    // @NotBlank(message = "El nombre de usuario es obligatorio")
    // private String username;

    // @NotBlank(message = "La contraseña es obligatoria")
    // @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    // private String password;

    // @NotNull(message = "El ID de la persona es obligatorio")
    // private long personID;

    // @Builder.Default
    // private boolean activo = true;
}
