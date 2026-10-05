package com.huellitas.vetTurno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Bean Validation: @Valid falló en un DTO de entrada
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ApiError(400, "Hay datos inválidos en la solicitud", errores));
    }

    // Reglas de negocio: fecha pasada, horario ocupado, referencia inexistente, email repetido
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarNegocio(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ApiError(400, ex.getMessage()));
    }

    // JSON mal formado o fecha con formato incorrecto
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ApiError(400,
                "El cuerpo de la solicitud no es válido. Revisa el JSON y el formato de las fechas (yyyy-MM-ddTHH:mm:ss)"));
    }

    // Id de la ruta con tipo incorrecto, por ejemplo /api/citas/veterinario/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> manejarTipoRuta(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(400, "El valor del parámetro '" + ex.getName() + "' no es válido"));
    }

    // Restricciones de la base de datos (por ejemplo, dos peticiones simultáneas)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad de datos", ex);
        return ResponseEntity.badRequest()
                .body(new ApiError(400, "Los datos entran en conflicto con registros existentes"));
    }

    // Login con credenciales incorrectas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> manejarCredenciales(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(401, "Email o contraseña incorrectos"));
    }

    // Se deja pasar para que Spring Security responda 403 y no lo capture el 500
    @ExceptionHandler(AccessDeniedException.class)
    public void manejarAccesoDenegado(AccessDeniedException ex) {
        throw ex;
    }

    // Cualquier fallo imprevisto: detalle solo en el log, mensaje genérico al cliente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGeneral(Exception ex) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(500, "Ocurrió un error interno. Intenta de nuevo más tarde"));
    }
}