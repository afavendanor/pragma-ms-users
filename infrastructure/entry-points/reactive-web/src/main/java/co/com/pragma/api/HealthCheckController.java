package co.com.pragma.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
@Tag(name = "HealthCheckController", description = "Entrada para verificar la salud de la aplicación")
public class HealthCheckController {

    @GetMapping(value = "/ping")
    @Operation(summary = "Health Check", description = "Verifica si el servicio está en funcionamiento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El servicio está funcionando correctamente.")
    })
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("pong");
    }
}
