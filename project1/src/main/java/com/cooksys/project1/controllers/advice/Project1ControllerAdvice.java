package com.cooksys.project1.controllers.advice;

import com.cooksys.project1.dtos.ErrorDto;
import com.cooksys.project1.exceptions.BadRequestException;
import com.cooksys.project1.exceptions.NotAuthorizedException;
import com.cooksys.project1.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = {"com.cooksys.project1.controllers"})
@ResponseBody
public class Project1ControllerAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedException.class)
    public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotAuthorizedException notAuthorizedException){
        return new ErrorDto(notAuthorizedException.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequestException(HttpServletRequest request, BadRequestException badRequestException){
        return new ErrorDto(badRequestException.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleBadRequestException(HttpServletRequest request, NotFoundException notFoundException){
        return new ErrorDto(notFoundException.getMessage());
    }
}
