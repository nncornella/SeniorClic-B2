package es.jlrn.presentation.dto.user;

import java.util.Set;

import es.jlrn.persistence.model.RoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class UserEntityDTO {
//
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres.")
    private String username;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    private String email;

    @NotNull(message = "El ID de la persona es obligatorio.")
    private Long personID;

    @NotEmpty(message = "Debe asignar al menos un rol.")
    private Set<@NotNull(message = "El rol no puede ser nulo.") RoleEntity> roles;

    private boolean activo; // Si necesitas validación lógica personalizada, podemos hacerlo

    // private Long id;

    // @NotBlank(message = "El nombre de usuario es obligatorio")
    // private String username;

    // @NotNull(message = "El ID de la persona es obligatorio")
    // private Long personID;

    // @NotEmpty(message = "Debe asignar al menos un rol")
    // private Set<RoleEntity> roles;
   
    // private boolean activo;
}

    