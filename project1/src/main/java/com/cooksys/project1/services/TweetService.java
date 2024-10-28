package com.cooksys.project1.services;

import com.cooksys.project1.dtos.*;

import java.util.List;

public interface TweetService {

    List<TweetResponseDto> getAllTweets();

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto);

    List<HashtagDto> getTweetTags(Long id);

    List<UserResponseDto> getUserMentions(Long id);

    List<TweetResponseDto> getTweetReplies(Long id);

    List<TweetResponseDto> getTweetReposts(Long id);

    void likeTweet(Long id, CredentialsDto credentialsDto);

    List<UserResponseDto> likedBy(Long id);

    TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);

    TweetResponseDto replyToTweet(Long id, TweetRequestDto tweetRequestDto);

    ContextDto getContext(Long id);
}
