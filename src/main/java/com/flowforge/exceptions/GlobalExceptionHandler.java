package com.flowforge.exceptions;

import com.flowforge.dto.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn("{} access through {}", exception.getMessage(), requestUrl);

        return ResponseEntity.status(statusCode)
                .body(new ApiResponseDto<>(
                        false,
                        statusCode.value(),
                        HttpStatus.valueOf(statusCode.value()),
                        extractDefaultMessage(exception.getMessage())));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity handleRecordNotFoundExceptions(RecordNotFoundException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn("{} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND,
                        exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST,
                        extractDefaultMessage(exception.getMessage())));
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity handleBadRequestExceptions(HttpMessageNotReadableException exception, WebRequest webRequest) {
//        String requestUrl = webRequest.getContextPath();
//        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ApiResponseDto<>(
//                        false,
//                        HttpStatus.BAD_REQUEST.value(),
//                        HttpStatus.BAD_REQUEST,
//                        extractDefaultMessage(exception.getMessage())));
//    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity handleConflictException(ConflictException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT,
                        extractDefaultMessage(exception.getMessage())));
    }


    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity handleDuplicateRecordExceptions(DuplicateRecordException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT,
                        exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedExceptions(UnauthorizedException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED,
                        exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedExceptions(AccessDeniedException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN,
                        exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentExceptions(BadRequestException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST,
                        extractDefaultMessage(exception.getMessage())));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchExceptions(MethodArgumentTypeMismatchException exception, WebRequest webRequest) {
        String requestUrl = webRequest.getContextPath();
        log.warn(" {} access through {}", exception.getMessage(), requestUrl);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST,
                        extractDefaultMessage(exception.getMessage())));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handlerGlobalErrors(Exception exception) {
        //log.warn("An error occur  {}", exception.fillInStackTrace());
        log.warn("An error occur  {}", exception.getMessage());
        //exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDto<>(
                        false,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST,
                        extractDefaultMessage(exception.getMessage())));
    }

    public static String extractDefaultMessage(String errorMessage) {
        try {
            // Pattern to match all default message parts
            Pattern pattern = Pattern.compile("default message \\[(.*?)]");
            Matcher matcher = pattern.matcher(errorMessage);
            String lastMessage = errorMessage; // fallback to return original if no match

            // Find all matches and keep the last one
            while (matcher.find()) {
                lastMessage = matcher.group(1);
            }
            if (lastMessage.equalsIgnoreCase(errorMessage)) {
                return extractValidationErrorMessage(lastMessage);
            } else
                return lastMessage;
        } catch (Exception e) {
            //e.printStackTrace();
            return errorMessage;
        }
    }

    public static String extractValidationErrorMessage(String errorString) {
        try {
            String pattern = "messageTemplate='(.*?)'";
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(errorString);

            String lastMessage = errorString;

            // Find all matches and keep the last one
            while (matcher.find()) {
                lastMessage = matcher.group(1);
            }
            return lastMessage;
        } catch (Exception e) {
            //e.printStackTrace();
            return errorString;
        }
    }
}

