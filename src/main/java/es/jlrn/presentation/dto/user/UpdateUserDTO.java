package es.jlrn.presentation.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
//
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres.")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    //@Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}",
    //        message = "La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula, un número y un carácter especial.")
    private String password;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    private String email;

    @NotNull(message = "El ID de la persona es obligatorio.")
    private Long personID;

    @NotBlank(message = "El nombre del rol es obligatorio.")
    private String roleName;

    @NotNull(message = "El estado de la cuenta (activo) es obligatorio.")
    private Boolean activo; // Podrías usar `boolean` si no quieres permitir valores nulos


    // @NotBlank(message = "El nombre de usuario es obligatorio")    
    // private String username;

    // @NotBlank(message = "La contraseña es obligatoria")
    // @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    // private String password;

    // @NotNull(message = "El ID de la persona es obligatorio")
    // private Long personID;    // ID de la persona

    // @NotNull(message = "El ID de la persona es obligatorio")
    // private String roleName;  // Nombre del rol
    
    // @NotNull
    // private Boolean activo;
}
