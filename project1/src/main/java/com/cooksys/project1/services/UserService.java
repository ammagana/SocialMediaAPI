package com.cooksys.project1.services;


import com.cooksys.project1.dtos.*;
import com.cooksys.project1.embeddables.Credentials;
import com.cooksys.project1.entities.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface UserService {

     UserResponseDto[] getAllUsers();

     UserResponseDto getUserByUsername(String username);

    UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto);

    UserResponseDto deleteUser(String username, UserRequestDto userRequestDto);

    void followUser(String username, CredentialsDto credentialsDto);

     UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getFollowing(String username);

    List<UserResponseDto> getFollowers(String username);

    List<TweetResponseDto> getMentions(String username);

    List<TweetResponseDto> getFeed(String username);

    List<TweetResponseDto> getUserTweets(String username);

    void unfollowUser(String username, CredentialsDto credentialsDto);

}
