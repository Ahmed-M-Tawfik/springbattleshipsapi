package com.altawfik.springbattleshipsapi.errorhandling.controller;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundExceptionBuilder;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.AccessDeniedExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.BindingResultExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.ExpectedExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.NestedRuntimeExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.mapper.UnexpectedExceptionMapper;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import com.altawfik.springbattleshipsapi.errorhandling.model.ErrorEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseErrorHandlerControllerTest {

    private BaseErrorHandlerController baseErrorHandlerController;

    @Mock
    private UnexpectedExceptionMapper unexpectedExceptionMapper;
    @Mock
    private ExpectedExceptionMapper expectedExceptionMapper;
    @Mock
    private BindingResultExceptionMapper bindingResultExceptionMapper;
    @Mock
    private NestedRuntimeExceptionMapper nestedRuntimeExceptionMapper;
    @Mock
    private AccessDeniedExceptionMapper accessDeniedExceptionMapper;
    @Mock
    private ApplicationEventPublisher publisher;

    @BeforeEach
    void setup() {
        baseErrorHandlerController = new BaseErrorHandlerController(unexpectedExceptionMapper, expectedExceptionMapper,
                bindingResultExceptionMapper, nestedRuntimeExceptionMapper, accessDeniedExceptionMapper, publisher);
    }

    @Test
    void handleContentException() {
        UUID id = UUID.randomUUID();
        ContentException contentException = new BattleNotFoundExceptionBuilder(id).build();

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(expectedExceptionMapper.map(contentException)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleContentException(contentException);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleAccessDeniedException() {
        AccessDeniedException accessDeniedException = new AccessDeniedException("message");

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(accessDeniedExceptionMapper.map(accessDeniedException)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleAccessDeniedException(accessDeniedException);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleRuntimeException() {
        RuntimeException runtimeException = new RuntimeException();

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(unexpectedExceptionMapper.map(runtimeException)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleRuntimeException(runtimeException);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleException() {
        Exception exception = new Exception();

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(unexpectedExceptionMapper.map(exception)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleException(exception);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException methodArgumentNotValidExceptionMock = mock(MethodArgumentNotValidException.class);

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(bindingResultExceptionMapper.map(methodArgumentNotValidExceptionMock)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleMethodArgumentNotValidException(methodArgumentNotValidExceptionMock);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleWebExchangeBindException() {
        WebExchangeBindException webExchangeBindExceptionMock = mock(WebExchangeBindException.class);

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(bindingResultExceptionMapper.map(webExchangeBindExceptionMock)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleWebExchangeBindException(webExchangeBindExceptionMock);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleServerWebInputException() {
        ServerWebInputException serverWebInputExceptionMock = mock(ServerWebInputException.class);

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(nestedRuntimeExceptionMapper.map(serverWebInputExceptionMock)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleServerWebInputException(serverWebInputExceptionMock);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleHttpMessageNotReadableException() {
        HttpMessageNotReadableException httpMessageNotReadableExceptionMock = mock(HttpMessageNotReadableException.class);

        BaseResponse baseResponse = new BaseResponse(new AppError());
        when(nestedRuntimeExceptionMapper.map(httpMessageNotReadableExceptionMock)).thenReturn(baseResponse);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleHttpMessageNotReadableException(httpMessageNotReadableExceptionMock);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void handleConstraintViolationException() {
        ConstraintViolationException constraintViolationExceptionMock = mock(ConstraintViolationException.class);

        String exceptionMessage = "someMessage";
        when(constraintViolationExceptionMock.getMessage()).thenReturn(exceptionMessage);

        ResponseEntity<BaseResponse> baseResponseEntity = baseErrorHandlerController.handleConstraintViolationException(constraintViolationExceptionMock);
        assertThat(baseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(baseResponseEntity.getBody()).isNotNull();
        assertThat(baseResponseEntity.getBody().getError().getMessage()).isEqualTo(exceptionMessage);
        assertThat(baseResponseEntity.getBody().getError().getOrigin()).isEqualTo("CONSTRAINT_VIOLATION");
        assertThat(baseResponseEntity.getBody().getError().getHttpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        verify(publisher).publishEvent(any(ErrorEvent.class));
    }

    @Test
    void publishErrorEvent() {
        baseErrorHandlerController.publishErrorEvent(new AppError(), new Throwable());

        verify(publisher).publishEvent(any(ErrorEvent.class));
    }
}