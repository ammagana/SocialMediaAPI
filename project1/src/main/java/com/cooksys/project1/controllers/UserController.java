package com.cooksys.project1.controllers;

import com.cooksys.project1.dtos.*;
import com.cooksys.project1.embeddables.Credentials;
import com.cooksys.project1.entities.User;
import com.cooksys.project1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserResponseDto[] getAllUsers(){ return userService.getAllUsers(); }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable("username") String username){ return userService.getUserByUsername(username); }

    @PatchMapping("/@{username}")
    public  UserResponseDto updateUserProfile(@PathVariable("username") String username, @RequestBody UserRequestDto userRequestDto){ return userService.updateUserProfile(username, userRequestDto); }

   //DOUBLE CHECK
    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@PathVariable("username") String username, @RequestBody UserRequestDto userRequestDto){ return userService.deleteUser(username, userRequestDto); }

    @PostMapping("@{username}/follow")
    public void followUser(@PathVariable("username") String username, @RequestBody CredentialsDto credentialsDto){ userService.followUser(username, credentialsDto); }

    @PostMapping
    @ResponseBody
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto){ return userService.createUser(userRequestDto); }

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getFollowing(@PathVariable("username") String username){ return userService.getFollowing(username); }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getFollowers(@PathVariable("username") String username){ return userService.getFollowers(username); }

    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getMentions(@PathVariable("username") String username){ return userService.getMentions(username); }

    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getFeed(@PathVariable("username") String username){
        return userService.getFeed(username);
    }

   @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable("username") String username){ return userService.getUserTweets(username); }

    @PostMapping("/@{username}/unfollow")
    public void unfollowUser(@PathVariable("username") String username, @RequestBody CredentialsDto credentialsDto){ userService.unfollowUser(username, credentialsDto); }

}
