package co.com.pragma.model.util;


public enum ResponseCode {
    MSUS000(500, "Ocurrió un error inesperado, por favor intenta mas tarde."),
    MSUS001(200, "Operación exitosa."),
    MSUS002(400, "Campos no son validos."),
    MSUS003(400, "Elemento o entidad a guardar o actualizar ya existe en la app.");

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
