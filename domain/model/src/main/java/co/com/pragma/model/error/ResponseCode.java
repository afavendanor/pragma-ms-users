package co.com.pragma.model.error;


public enum ResponseCode {
    MSUS000("Ocurrió un error inesperado, por favor intenta mas tarde."),
    MSUS001("Operación exitosa."),
    MSUS002("Campos no son validos."),
    MSUS003("El correo a registrar ya existe en la app."),
    MSUS004("La entidad a registrar ya existe en la app."),
    MSUS005("El usuario no tiene perfil asignado."),
    MSUS006("No se encontraron datos del usario registrados."),
    MSUS007("Credenciales inválidas.");

    private final String message;

    ResponseCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
