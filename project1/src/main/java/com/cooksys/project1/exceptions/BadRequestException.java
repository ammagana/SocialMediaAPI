package com.cooksys.project1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException{
    private String message;
    private static final long serialVersionUID = 1962717895811129369L;
}
