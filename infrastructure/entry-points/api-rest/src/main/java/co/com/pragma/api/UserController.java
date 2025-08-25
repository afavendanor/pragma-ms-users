package co.com.pragma.api;

import co.com.pragma.api.dto.CreateUserDTO;
import co.com.pragma.api.dto.GenericResponseDTO;
import co.com.pragma.api.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@Tag(name = "UserController", description = "Entrada para las operaciones relacionadas al modelo de usuario")
@Validated
public class UserController {

    private final UserHandler userHandler;

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Agregar usuario", description = "Permite recibir una petición de agregar un usuario. Este evalua los campos obligatorios, existencia y formatos para antes de crear el elemento en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos recibidos no cumplen con la obligatoriedad o formatos esperados", content = @Content(schema = @Schema(implementation = GenericResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error inesperado durante el proceso", content = @Content(schema = @Schema(implementation = GenericResponseDTO.class)))})
    public Mono<ResponseEntity<GenericResponseDTO<Object>>> saveUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return userHandler.createUser(createUserDTO)
                .map(genericResponseDto -> ResponseEntity.status(genericResponseDto.getStatus()).body(genericResponseDto));

    }

}
