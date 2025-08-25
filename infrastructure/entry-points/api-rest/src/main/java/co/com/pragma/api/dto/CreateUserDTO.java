package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Schema(description = "Modelo de entrada para crear un usuario.")
public class CreateUserDTO {
    @NotBlank(message = "Identificaciòn es requerida.")
    @Schema(description = "Identificaciòn del usuario.")
    private String identification;
    @NotBlank(message = "Nombre es requerido.")
    @Schema(description = "Nombre del usuario.")
    private String name;
    @NotBlank(message = "Appellido es requerida")
    @Schema(description = "Apellido del usuario")
    private String lastName;
    @Schema(description = "Fecha de nacimiento del usuario.")
    private LocalDate birthDay;
    @Schema(description = "Direcciòn del usuario.")
    private String address;
    @Schema(description = "Telèfono del usuario.")
    private String phone;
    @NotBlank(message = "Correo es requerido.")
    @Email(message = "El cvalor del correo no es vàlido.")
    @Schema(description = "Correo del usuario.")
    private String email;
    @NotNull(message = "Salario base es requerida.")
    @Min(value = 0, message = "El valor del salario base debe ser mayor que 0.")
    @Max(value = 15000000, message = "El valor del salario base debe ser menor o igual a 15000000.")
    @Schema(description = "Salario base del usuario.")
    private Double baseSalary;
}
