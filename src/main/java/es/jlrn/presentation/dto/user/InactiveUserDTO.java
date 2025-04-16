package es.jlrn.presentation.dto.user;

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
public class InactiveUserDTO {
//
    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres.")
    private String username;

    @NotBlank(message = "El nombre de la persona es obligatorio.")
    @Size(max = 150, message = "El nombre de la persona no puede exceder los 150 caracteres.")
    private String personName; // nombre completo

//     private Long id;

//     @NotBlank(message = "La contraseña es obligatoria")
//     @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
//     private String username;

//     @NotBlank(message = "El nombre de la persona es obligatorio")
//     private String personName; // nombre completo, o como lo manejes
 }
