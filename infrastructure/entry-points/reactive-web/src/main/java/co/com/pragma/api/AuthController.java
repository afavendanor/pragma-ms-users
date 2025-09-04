package co.com.pragma.api;

import co.com.pragma.api.dto.GenericResponseDTO;
import co.com.pragma.api.dto.RequestLoginDTO;
import co.com.pragma.api.handler.AuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@Tag(name = "UserController", description = "Entrada para las operaciones relacionadas al modelo de usuario")
@Validated
public class AuthController {

    private final AuthHandler authHandler;

    @PostMapping(value = "/login")
    @Operation(summary = "Login de usuario", description = "Permite recibir una petición de login de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos recibidos no cumplen con la obligatoriedad o formatos esperados", content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error inesperado durante el proceso", content = @Content(schema = @Schema(implementation = GenericResponseDTO.class)))})
    public Mono<ResponseEntity<GenericResponseDTO<Map<String, Object>>>> login(@Valid @RequestBody RequestLoginDTO requestLoginDTO) {
        return authHandler.login(requestLoginDTO)
                .map(genericResponseDto -> ResponseEntity.status(genericResponseDto.getResponseCode()).body(genericResponseDto));

    }

}
