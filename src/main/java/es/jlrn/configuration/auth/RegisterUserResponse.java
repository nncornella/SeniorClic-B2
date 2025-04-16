package es.jlrn.configuration.auth;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserResponse {
//
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    // @NotBlank(message = "La contraseña es obligatoria")
    // private String password;

    @NotNull(message = "El ID de la persona es obligatorio")
    private long personID;

    @Builder.Default
    private Set<String> roles = new HashSet<>(Set.of("ROLE_USER"));

    @Builder.Default
    private boolean activo = true;
}
