package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "Modelo de salida para datos de usuario.")
public class UserDTO {
    @Schema(description = "Identificaciòn del usuario.")
    private String identification;
    @Schema(description = "Nombre del usuario.")
    private String name;
    @Schema(description = "Apellido del usuario.")
    private String lastName;
    @Schema(description = "Fecha de nacimiento del usuario.")
    private LocalDate birthDay;
    @Schema(description = "Direcciòn del usuario.")
    private String address;
    @Schema(description = "Telèfono del usuario.")
    private String phone;
    @Schema(description = "Correo electrònico del usuario.")
    private String email;
    @Schema(description = "Salario base del usuario.")
    private Double baseSalary;
}
