package com.itheima.exception;

import com.itheima.controller.ResponseData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleServerException(Exception ex, WebRequest request) {
                Integer code = 500;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(ex.getClass()==BadRequestException.class){
            code = 400;
            status = HttpStatus.BAD_REQUEST;
        }
        ResponseData<Object> responseData = new ResponseData<>(null, ex.getMessage(),code);
        return handleExceptionInternal(ex, responseData,new HttpHeaders(),status,request);
    }
}
