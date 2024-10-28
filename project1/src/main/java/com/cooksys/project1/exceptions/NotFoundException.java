package com.cooksys.project1.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException{

    private  static final long serialVersionUID = 6580296965767415034L;
    private String Message;
}
