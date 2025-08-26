package co.com.pragma.model.error;


public enum ResponseCode {
    MSUS000(500, "Ocurrió un error inesperado, por favor intenta mas tarde."),
    MSUS001(200, "Operación exitosa."),
    MSUS002(400, "Campos no son validos."),
    MSUS003(400, "El correo a registrar ya existe en la app."),
    MSUS004(400, "La entidad a registrar ya existe en la app.");

    private final int status;
    private final String htmlMessage;

    ResponseCode(int status, String htmlMessage) {
        this.status = status;
        this.htmlMessage = htmlMessage;
    }

    public int getStatus() {
        return this.status;
    }

    public String getHtmlMessage() {
        return this.htmlMessage;
    }
}
