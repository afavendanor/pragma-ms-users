package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de entrada para crear un usuario.")
public class CreateUserDTO {
    @NotBlank(message = "Identificación es requerida.")
    @Schema(description = "Identificación del usuario.")
    private String identification;

    @NotBlank(message = "Nombre es requerido.")
    @Schema(description = "Nombre del usuario.")
    private String name;

    @NotBlank(message = "Apellido es requerido.")
    @Schema(description = "Apellido del usuario.")
    private String lastName;

    @Schema(description = "Fecha de nacimiento del usuario (yyyy-MM-dd).")
    private LocalDate birthDay;

    @Schema(description = "Dirección del usuario.")
    private String address;

    @Schema(description = "Teléfono del usuario.")
    private String phone;

    @NotBlank(message = "Correo es requerido.")
    @Email(message = "El valor del correo no es válido.")
    @Schema(description = "Correo del usuario.")
    private String email;

    @NotNull(message = "Salario base es requerido.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor que 0.")
    @DecimalMax(value = "15000000.0", message = "El salario debe ser menor o igual a 15000000.")
    @Schema(description = "Salario base del usuario.")
    private Double baseSalary;

    @NotBlank(message = "La contraseña es requerida.")
    @Schema(description = "Contraseña del usuario.")
    private String password;
}
