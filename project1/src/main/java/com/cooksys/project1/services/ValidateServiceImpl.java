package com.cooksys.project1.services;

import com.cooksys.project1.entities.User;
import com.cooksys.project1.exceptions.NotFoundException;
import com.cooksys.project1.repositories.HashtagRepository;
import com.cooksys.project1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService{

    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public boolean doesUsernameExist(String username) {
        Optional<User> thisUser = userRepository.findByCredentialsUsername(username);
        if(thisUser.isEmpty()){
            throw new NotFoundException("No user found with username: " + username);
        }
        User user = thisUser.get();
        if(user.getCredentials().getUsername().equals(username)){
            return true;
        }
        return false;
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        Optional<User> thisUser = userRepository.findByCredentialsUsername(username);
        if(thisUser.isEmpty()){
            return true;
        }
        User user = thisUser.get();
        if(user.getDeleted() == true){
            return true;
        }
        return false;
    }

    @Override
    public Boolean doesTagExist(String tag) {
        return hashtagRepository.existsByLabel(tag);
    }

    @Override
    public Boolean randomTagExists() {
        return (hashtagRepository.findAll().size() != 0);
    }

}
