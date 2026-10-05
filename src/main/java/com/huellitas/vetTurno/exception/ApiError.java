package com.huellitas.vetTurno.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiError {

    private int status;
    private String mensaje;
    private Map<String, String> errores = new LinkedHashMap<>();
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError() {
    }

    public ApiError(int status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }

    public ApiError(int status, String mensaje, Map<String, String> errores) {
        this.status = status;
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Map<String, String> getErrores() { return errores; }
    public void setErrores(Map<String, String> errores) { this.errores = errores; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}