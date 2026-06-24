package br.com.gmob.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Pattern UNIQUE_CONSTRAINT_PATTERN =
            Pattern.compile("unique constraint \"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern FOREIGN_KEY_PATTERN =
            Pattern.compile("foreign key constraint \"([^\"]+)\"", Pattern.CASE_INSENSITIVE);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                "Not Found"
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                "Conflict"
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        return buildResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                "Forbidden"
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                "Bad Request"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                messages,
                "Bad Request"
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida ou corpo mal formatado",
                "Bad Request"
        );
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            EmptyResultDataAccessException.class
    })
    public ResponseEntity<ErrorResponse> handleEntityNotFound(RuntimeException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "A operação falhou porque o registro que você tentou modificar ou deletar não foi encontrado.",
                "Not Found"
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        String rootMessage = extractRootMessage(ex);
        String lowerMessage = rootMessage.toLowerCase(Locale.ROOT);

        if (isUniqueViolation(lowerMessage)) {
            String fieldName = extractConstraintField(rootMessage, UNIQUE_CONSTRAINT_PATTERN);
            String message = fieldName != null
                    ? "Já existe um registro com este valor. O campo '" + fieldName + "' deve ser único."
                    : "Já existe um registro com este valor.";
            return buildResponse(HttpStatus.CONFLICT, message, "Conflict");
        }

        if (isForeignKeyViolation(lowerMessage)) {
            String fieldName = extractConstraintField(rootMessage, FOREIGN_KEY_PATTERN);
            String message = fieldName != null
                    ? "A operação falhou porque o valor fornecido para '" + fieldName + "' não existe em um registro relacionado."
                    : "A operação falhou porque o valor fornecido não existe em um registro relacionado.";
            return buildResponse(HttpStatus.BAD_REQUEST, message, "Bad Request");
        }

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "A operação falhou devido a uma restrição de integridade nos dados.",
                "Bad Request"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado no servidor.",
                "Internal Server Error"
        );
    }

    private String formatFieldError(FieldError fieldError) {
        String defaultMessage = fieldError.getDefaultMessage();
        if (defaultMessage != null && !defaultMessage.isBlank()) {
            return defaultMessage;
        }
        return fieldError.getField() + " é inválido";
    }

    private boolean isUniqueViolation(String lowerMessage) {
        return lowerMessage.contains("duplicate key")
                || lowerMessage.contains("unique constraint")
                || lowerMessage.contains("already exists")
                || lowerMessage.contains("violates unique");
    }

    private boolean isForeignKeyViolation(String lowerMessage) {
        return lowerMessage.contains("foreign key")
                || lowerMessage.contains("violates foreign key")
                || lowerMessage.contains("still referenced");
    }

    private String extractRootMessage(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String message = cause != null ? cause.getMessage() : ex.getMessage();
        return message != null ? message : "";
    }

    private String extractConstraintField(String message, Pattern pattern) {
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            Object message,
            String error
    ) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), message, error));
    }
}
