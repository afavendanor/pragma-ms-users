package co.com.pragma.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Modelo de entrada para login un usuario.")
public class RequestLoginDTO {
    @NotBlank(message = "Correo es requerida.")
    @Schema(description = "Correo del usuario.")
    private String email;
    @NotBlank(message = "Contraseña es requerida.")
    @Schema(description = "Contraeña del usuario.")
    private String password;
}
