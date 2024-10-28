package com.cooksys.project1.services;

public interface ValidateService {
    boolean doesUsernameExist(String username);

    boolean isUsernameAvailable(String username);

    Boolean doesTagExist(String label);

    Boolean randomTagExists();

    //boolean doesUsernameExist(String username);


}
