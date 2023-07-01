package com.seerbit.centricgateway.task.core.exceptions;



import com.seerbit.centricgateway.task.utilities.HTTPMessagesMap;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ExceptionsHandler extends ResponseEntityExceptionHandler {


    private Map<String, Object> buildError(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", false);
        response.put("timestamp", new Date());
        response.put("data", null);

        return response;
    }

    private Map<String, Object> buildError(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", false);
        response.put("timestamp", new Date());
        response.put("data", data);

        return response;
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        String message = ex.getLocalizedMessage();
        log.error(ex.getMessage());
        return getResponseEntity(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException cve) {
        return getResponseEntity(HTTPMessagesMap.ERR_VALIDATION.getMessage(),
                getValidationErrors(cve.getConstraintViolations()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
    

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getResponseEntity(HTTPMessagesMap.ERR_VALIDATION.getMessage(),
                getBodyValidationErrors(ex.getBindingResult().getFieldErrors()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        String allowedMethods = StringUtils.collectionToCommaDelimitedString(supportedMethods);
        String message = String.format("%s %s", HTTPMessagesMap.ERR_METHOD_NOT_SUPPORTED.getMessage(), allowedMethods);
        return handleExceptionInternal(ex, buildError(message), headers, status, request);
    }
    
    
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(HTTPMessagesMap.ERR_MEDIA_TYPE_NOT_SUPPORTED.getMessage());
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return getResponseEntity(builder.substring(0, builder.length() - 2), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    
    
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getResponseEntity(HTTPMessagesMap.ERR_JSON_RESPONSE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getResponseEntity(HTTPMessagesMap.ERR_JSON_REQUEST.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getResponseEntity(String apiResponse, HttpStatus status) {
        return new ResponseEntity<>(buildError(apiResponse), status);
    }

    private ResponseEntity<Object> getResponseEntity(String message, Object response, HttpStatus status) {
        return new ResponseEntity<>(buildError(message, response), status);
    }

    private List<String> getBodyValidationErrors(List<FieldError> list) {
        List<String> errors = new ArrayList<>();
        list.forEach(e -> errors.add(e.getDefaultMessage()));
        return errors;
    }

    private Map<String, String> getValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        Map<String, String> errors = new HashMap<>();
        constraintViolations.forEach(e -> errors.put( ((PathImpl) e.getPropertyPath()).getLeafNode().asString(), e.getMessage()));
        return errors;
    }
    
}
