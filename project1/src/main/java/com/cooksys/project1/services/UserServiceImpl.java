package com.cooksys.project1.services;

import com.cooksys.project1.dtos.*;
import com.cooksys.project1.embeddables.Credentials;
import com.cooksys.project1.embeddables.Profile;
import com.cooksys.project1.entities.Tweet;
import com.cooksys.project1.entities.User;
import com.cooksys.project1.exceptions.BadRequestException;
import com.cooksys.project1.exceptions.NotAuthorizedException;
import com.cooksys.project1.exceptions.NotFoundException;
import com.cooksys.project1.mappers.CredentialsMapper;
import com.cooksys.project1.mappers.ProfileMapper;
import com.cooksys.project1.mappers.TweetMapper;
import com.cooksys.project1.mappers.UserMapper;
import com.cooksys.project1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private  final UserRepository userRepository;
    private final UserMapper userMapper;
    private  final CredentialsMapper credentialsMapper;
    private  final ProfileMapper profileMapper;
    private final TweetMapper tweetMapper;

    @Override
    public UserResponseDto[] getAllUsers() {
        return  userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse()).toArray(new UserResponseDto[0]);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        Optional<User> thisUser = userRepository.findByCredentialsUsername(username);
        if(thisUser.isEmpty()){
            throw new NotFoundException("user does not exist by username: " + username);
        }
        User user = thisUser.get();
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUserProfile(String username, UserRequestDto userRequestDto) {
        Credentials credRequest = credentialsMapper.dtoToCredentials(userRequestDto.getCredentials());
        Profile profileRequest = profileMapper.dtoToEntity(userRequestDto.getProfile());
        Optional<User> thisUser = userRepository.findByCredentialsUsername(username);
        System.out.println("UserRequestDto: " + userRequestDto);
        if(thisUser.isEmpty()){
            throw new NotFoundException("user does not exist by username: " + username);
        }
        User userToUpdate = thisUser.get();

        if(userToUpdate.getDeleted() == true){
            throw new NotFoundException("user was deleted and does not exist by username: " + username);
        }
        if(credRequest == null){
            throw new BadRequestException("Bad Request: No credentials");
        }
        if(profileRequest == null){
            throw new BadRequestException("Bad Request: No profile");
        }

        String userUsername = userToUpdate.getCredentials().getUsername();
        String userPassword = userToUpdate.getCredentials().getPassword();
         String requestUsername = credRequest.getUsername();
         String requestPassword = credRequest.getPassword();

        if(userUsername.equals(requestUsername) == false){
            throw new NotFoundException("Invalid credentials: username mismatch");
        }
        if(userPassword.equals(requestPassword) == false){
            throw new NotFoundException("Invalid credentials: password mismatch");
        }

        userToUpdate.setProfile(profileRequest);
        userRepository.saveAndFlush(userToUpdate);
        return userMapper.entityToDto(userToUpdate);

    }

    @Override
    public UserResponseDto deleteUser(String username, UserRequestDto userRequestDto) {
        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
        Credentials credentials = credentialsMapper.dtoToCredentials(userRequestDto.getCredentials());
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User does not exist by username: " + username);
        }
        if(credentials == null){
            throw new BadRequestException("Null credentials");
        }
        User user = optionalUser.get();
        if((user.getCredentials().getUsername()).equals(credentials.getUsername()) == false ||user.getCredentials().getPassword().equals(credentials.getPassword()) == false){
            throw new NotAuthorizedException("Username or password is invalid");
        }
        user.setDeleted(true);
        userRepository.saveAndFlush(user);
        return userMapper.entityToDto(user);
    }

    @Override
    public void followUser(String username, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsMapper.dtoToCredentials(credentialsDto);
        System.out.println("Credentials: " + credentials);
        if(credentials == null){
            throw new BadRequestException("Null credentials");
        }

        Optional<User> optionalUser = userRepository.findByCredentialsUsername(credentials.getUsername());
        Optional<User> optionalUser2 = userRepository.findByCredentialsUsername(username);

        if(optionalUser.isEmpty()){
            throw new NotFoundException("User does not exist by username: " + credentials.getUsername());
        }
        if(optionalUser2.isEmpty()){
            throw new NotFoundException("UserToFollow does not exist by username: " + username);
        }
        User thisUser = optionalUser.get();
        User userToFollow = optionalUser2.get();
        if(!(thisUser.getCredentials().getUsername()).equals(credentials.getUsername()) || !thisUser.getCredentials().getPassword().equals(credentials.getPassword())){
            throw new NotAuthorizedException("Username or password is invalid");
        }
        if(userToFollow.getDeleted()){
            throw new NotFoundException("UserToFollow does not exist by username: " + username);
        }
        List<User> followingOfUser1 = new ArrayList<>(thisUser.getFollowing());
        List<User> followersOfUser2 = new ArrayList<>(userToFollow.getFollowers());
        if(followingOfUser1.contains(userToFollow) && followersOfUser2.contains(thisUser)){
            throw new BadRequestException("Cannot fulfill request: user1 already follows user2");
        }
        thisUser.getFollowing().add(userToFollow);
        userToFollow.getFollowers().add(thisUser);
        userRepository.saveAndFlush(thisUser);
        userRepository.saveAndFlush(userToFollow);

    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        Credentials credentials = credentialsMapper.dtoToCredentials(userRequestDto.getCredentials());
        Profile profile = profileMapper.dtoToEntity(userRequestDto.getProfile());
        User newUser = userMapper.dtoToEntity(userRequestDto);

        Optional<User> findUser = userRepository.findByCredentials(credentials);
        if(!findUser.isEmpty()){
            newUser = findUser.get();
            newUser.setDeleted(false);
            userRepository.saveAndFlush(newUser);
            return userMapper.entityToDto(newUser);
        }

        if(credentials == null || credentials.getPassword() == null || credentials.getUsername() == null){
            throw new BadRequestException("Cannot fulfill request: invalid credentials");
        }
        if(profile == null || profile.getFirstName() == null || profile.getLastName() == null || profile.getEmail() == null || profile.getPhone() == null){
            throw new BadRequestException("Cannot fulfill request: invalid credentials");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        newUser.setJoined(currentDateTime);
        userRepository.saveAndFlush(newUser);
        return userMapper.entityToDto(newUser);
    }

    @Override
    public List<UserResponseDto> getFollowing(String username) {
        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User does not exist by username: " + username);
        }
        User user = optionalUser.get();
        if(user.getDeleted()){
            throw new NotFoundException("User deleted by username: " + username);
        }
        List<User> followingList = new ArrayList<>(user.getFollowing());
        List<UserResponseDto> returnList = new ArrayList<>();
        for(User followedUser : followingList){
            returnList.add(userMapper.entityToDto(followedUser));
        }

        return returnList;
    }

    @Override
    public List<UserResponseDto> getFollowers(String username) {
        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User does not exist by username: " + username);
        }
        User user = optionalUser.get();
        if(user.getDeleted()){
            throw new NotFoundException("User deleted by username: " + username);
        }
        List<User> followers = new ArrayList<>(user.getFollowers());
        List<UserResponseDto> returnList = new ArrayList<>();
        for(User follower : followers){
            returnList.add(userMapper.entityToDto(follower));
        }

        return returnList;
    }

    @Override
    public List<TweetResponseDto> getMentions(String username) { //CHECK
        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User does not exist by username: " + username);
        }
        User user = optionalUser.get();
        if(user.getDeleted()){
            throw new NotFoundException("User deleted by username: " + username);
        }
        List<Tweet> mentions = new ArrayList<>(user.getMentions());
        List<TweetResponseDto> tweetList = new ArrayList<>();
        Collections.sort(mentions, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet tweet1, Tweet tweet2) {
                return tweet2.getPosted().compareTo(tweet1.getPosted()); // Compare in reverse order
            }
        });
        for(Tweet tweet : mentions){
            if(tweet.getDeleted() == false) {
                tweetList.add(tweetMapper.entityToDto(tweet));
            }
        }
        return tweetList;
    }

    @Override
    public List<TweetResponseDto> getFeed(String username) {
        List<TweetResponseDto> tweetFeed = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
        if (optionalUser == null) {
            throw new NotFoundException("User not found");
        } else {
            User user = optionalUser.get();
            if (user.getDeleted() == true) {
                throw new NotFoundException("User not found");
            } else {
                for (Tweet tweet : user.getTweets()) {
                    tweetFeed.add(tweetMapper.entityToDto(tweet));
                }
                for (UserResponseDto userDto : getFollowers(username)) {
                    User userEntity = userMapper.dtoToEntity(userDto);
                    for (Tweet t : userEntity.getTweets()) {
                        tweetFeed.add(tweetMapper.entityToDto(t));
                    }
                }
            }
        }
        tweetFeed.sort((Tweet1, Tweet2) -> Tweet1.getPosted().compareTo(Tweet2.getPosted()));
        return tweetFeed;
    }
    @Override
    public List<TweetResponseDto> getUserTweets(String username) {
        Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User does not exist by username: " + username);
        }
        User user = optionalUser.get();
        if(user.getDeleted()){
            throw new NotFoundException("User deleted by username: " + username);
        }
        List<Tweet> tweets = new ArrayList<>(user.getTweets());
        List<TweetResponseDto> usersTweets = new ArrayList<>();
        Collections.sort(tweets, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet tweet1, Tweet tweet2) {
                return tweet2.getPosted().compareTo(tweet1.getPosted()); // Compare in reverse order
            }
        });
        for(Tweet tweet : tweets){
            if(tweet.getDeleted() == false) {
                usersTweets.add(tweetMapper.entityToDto(tweet));
            }
        }
        return usersTweets;

    }

    @Override
    public void unfollowUser(String username, CredentialsDto credentialsDto) {
        Credentials credentials = credentialsMapper.dtoToCredentials(credentialsDto);
        System.out.println("Credentials: " + credentials);
        if(credentials == null){
            throw new BadRequestException("Null credentials");
        }

        Optional<User> optionalUser = userRepository.findByCredentialsUsername(credentials.getUsername());
        Optional<User> optionalUser2 = userRepository.findByCredentialsUsername(username);

        if(optionalUser.isEmpty()){
            throw new NotFoundException("User does not exist by username: " + credentials.getUsername());
        }
        if(optionalUser2.isEmpty()){
            throw new NotFoundException("UserToFollow does not exist by username: " + username);
        }
        User thisUser = optionalUser.get();
        User userToUnfollow = optionalUser2.get();
        if(!(thisUser.getCredentials().getUsername()).equals(credentials.getUsername()) || !thisUser.getCredentials().getPassword().equals(credentials.getPassword())){
            throw new NotAuthorizedException("Username or password is invalid");
        }
        if(userToUnfollow.getDeleted()){
            throw new NotFoundException("UserToFollow does not exist by username: " + username);
        }
        List<User> followingOfUser1 = new ArrayList<>(thisUser.getFollowing());
        List<User> followersOfUser2 = new ArrayList<>(userToUnfollow.getFollowers());
        if(!followingOfUser1.contains(userToUnfollow) && !followersOfUser2.contains(thisUser)){
                throw new BadRequestException("Cannot unfollow: user1 does not follow user2");
        }
        thisUser.getFollowing().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(thisUser);
        userRepository.saveAndFlush(thisUser);
        userRepository.saveAndFlush(userToUnfollow);

    }
}
