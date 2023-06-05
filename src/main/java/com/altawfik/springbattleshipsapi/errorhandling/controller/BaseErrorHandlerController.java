package com.altawfik.springbattleshipsapi.errorhandling.controller;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.AccessDeniedExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.BindingResultExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.ExpectedExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.NestedRuntimeExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.UnexpectedExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import com.altawfik.springbattleshipsapi.errorhandling.model.ErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class BaseErrorHandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseErrorHandlerController.class);

    private final UnexpectedExceptionMapper unexpectedExceptionMapper;
    private final ExpectedExceptionMapper expectedExceptionMapper;
    private final BindingResultExceptionMapper bindingResultExceptionMapper;
    private final NestedRuntimeExceptionMapper nestedRuntimeExceptionMapper;
    private final AccessDeniedExceptionMapper accessDeniedExceptionMapper;
    private final ApplicationEventPublisher publisher;

    public BaseErrorHandlerController(final UnexpectedExceptionMapper unexpectedExceptionMapper,
                                      final ExpectedExceptionMapper expectedExceptionMapper,
                                      final BindingResultExceptionMapper bindingResultExceptionMapper,
                                      final NestedRuntimeExceptionMapper nestedRuntimeExceptionMapper,
                                      final AccessDeniedExceptionMapper accessDeniedExceptionMapper,
                                      final ApplicationEventPublisher publisher) {

        this.unexpectedExceptionMapper = unexpectedExceptionMapper;
        this.expectedExceptionMapper = expectedExceptionMapper;
        this.bindingResultExceptionMapper = bindingResultExceptionMapper;
        this.nestedRuntimeExceptionMapper = nestedRuntimeExceptionMapper;
        this.accessDeniedExceptionMapper = accessDeniedExceptionMapper;
        this.publisher = publisher;
    }

    @ExceptionHandler(ContentException.class)
    public ResponseEntity<BaseResponse> handleContentException(final ContentException e) {
        var baseResponse = expectedExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, e.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException(final AccessDeniedException e) {
        var baseResponse = accessDeniedExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse> handleRuntimeException(final RuntimeException e) {
        var baseResponse = unexpectedExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(final Exception e) {
        var baseResponse = unexpectedExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        var baseResponse = bindingResultExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, BAD_REQUEST);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<BaseResponse> handleWebExchangeBindException(final WebExchangeBindException e) {
        var baseResponse = bindingResultExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<BaseResponse> handleServerWebInputException(final ServerWebInputException e) {
        var baseResponse = nestedRuntimeExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        var baseResponse = nestedRuntimeExceptionMapper.map(e);
        publishErrorEvent(baseResponse.getError(), e);
        return new ResponseEntity<>(baseResponse, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleConstraintViolationException(final ConstraintViolationException e) {

        var error = new AppError();
        error.setMessage(e.getMessage());
        error.setHttpStatusCode(BAD_REQUEST.value());
        error.setOrigin("CONSTRAINT_VIOLATION");
        var baseResponse = new BaseResponse(error);

        publishErrorEvent(baseResponse.getError(), e);

        return new ResponseEntity<>(baseResponse, BAD_REQUEST);
    }

    protected void publishErrorEvent(final AppError error, final Throwable throwable) {
        var errorEvent = new ErrorEvent(error, throwable);
        LOGGER.error(throwable.getMessage(), throwable);
        publisher.publishEvent(errorEvent);
    }
}
