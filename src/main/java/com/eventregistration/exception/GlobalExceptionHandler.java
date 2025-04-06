package com.eventregistration.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.eventregistration.dto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception exception) {
        log.error("Uncategorized error: ", exception);
        return buildErrorResponse(ErrorCode.UNCATEGORIZED_EXCEPTION, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = ErrorCode.API_LACK_OF_PARAMETER.getMessage() + ": " + ex.getParameterName();
        return buildErrorResponse(ErrorCode.API_LACK_OF_PARAMETER, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            try {
                ErrorCode errorCode = ErrorCode.valueOf(message);
                Object[] arguments = error.getArguments();
                if (arguments != null && arguments.length > 1) {
                    // Giả sử argument[1] là giá trị như "min", "max" (tùy cấu hình validation)
                    message = String.format(errorCode.getMessage(), arguments[1]);
                } else {
                    message = errorCode.getMessage();
                }
            } catch (IllegalArgumentException e) {
                // Nếu không phải ErrorCode, giữ nguyên message mặc định
                log.warn("Invalid ErrorCode in validation: {}", message);
            }
            errors.put(field, message);
        });

        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .code(HttpStatus.BAD_REQUEST.value()) // Dùng mã HTTP thay vì mã nội bộ
                .message("Validation failed")
                .result(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Xử lý AppException
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String message = exception.getCustomMessage() != null ? exception.getCustomMessage() : errorCode.getMessage();
        return buildErrorResponse(errorCode, message);
    }

    // Xử lý AccessDeniedException từ Spring Security
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException exception) {
        log.error("Access denied: ", exception);
        return buildErrorResponse(ErrorCode.USER_UNAUTHORIZED, null);
    }

    // Helper method để xây dựng phản hồi lỗi
    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(ErrorCode errorCode, String customMessage) {
        ApiResponse<Object> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(customMessage != null ? customMessage : errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}
