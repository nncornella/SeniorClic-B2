package es.jlrn.presentation.dto.person;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePersonDTO {
//
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres.")
    private String surnames;

    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    private String email;

    @NotBlank(message = "La localidad no puede estar vacía.")
    @Size(max = 100, message = "La localidad no puede exceder los 100 caracteres.")
    private String locality;

    @NotBlank(message = "La provincia no puede estar vacía.")
    @Size(max = 100, message = "La provincia no puede exceder los 100 caracteres.")
    private String province;

    @NotNull(message = "La fecha de inicio no puede ser nula.")
    @PastOrPresent(message = "La fecha de inicio no puede estar en el futuro.")
    @Builder.Default
    private LocalDate startDate = LocalDate.now();

    @NotNull(message = "La fecha de último acceso no puede ser nula.")
    @PastOrPresent(message = "La fecha de último acceso no puede estar en el futuro.")
    @Builder.Default
    private LocalDate lastAccessDate = LocalDate.now();

    // @NotNull
    // @NotBlank(message = "El nombre no puede estar vacío")
    // @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    // private String name;

    // @NotNull
    // @NotBlank(message = "El apellido no puede estar vacío")
    // @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    // private String surnames;

    // @NotNull(message = "El email no puede ser nulo")
    // @Size(max = 150, message = "El correo electrónico no puede exceder los 150 caracteres")
    // @Email(message = "El correo electrónico debe tener un formato válido")
    // private String email;

    // @NotNull
    // @NotBlank(message = "La localidad no puede estar vacía")
    // @Size(max = 100, message = "La localidad no puede exceder los 100 caracteres")
    // private String locality;

    // @NotNull
    // @NotBlank(message = "La provincia no puede estar vacía")
    // @Size(max = 100, message = "La provincia no puede exceder los 100 caracteres")
    // private String province;

    // @NotNull(message = "La fecha de inicio no puede ser nula")
    // @PastOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
    // @Builder.Default
    // private LocalDate startDate = LocalDate.now();

    // @NotNull(message = "La fecha de último acceso no puede ser nula")
    // @PastOrPresent(message = "La última fecha de acceso no puede ser anterior a hoy")
    // @Builder.Default
    // private LocalDate lastAccessDate = LocalDate.now();
}
