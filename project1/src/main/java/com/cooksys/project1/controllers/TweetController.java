package com.cooksys.project1.controllers;

import com.cooksys.project1.dtos.*;
import com.cooksys.project1.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllTweets();
    }

    @PostMapping
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.createTweet(tweetRequestDto);
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable("id") Long id) {
        return tweetService.getTweetById(id);
    }

    @PostMapping("/{id}/like")
    public void likeTweet(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto) {
        tweetService.likeTweet(id, credentialsDto);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweet(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.deleteTweet(id, credentialsDto);
    }

    @PostMapping("/{id}/reply")
    public TweetResponseDto replyToTweet(@PathVariable("id") Long id, @RequestBody TweetRequestDto tweetRequestDto){
        return tweetService.replyToTweet(id, tweetRequestDto);
    }

    @GetMapping("/{id}/tags")
    public List<HashtagDto> getTweetTags(@PathVariable("id") Long id) {
        return tweetService.getTweetTags(id);
    }

    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getUserMentions(@PathVariable("id") Long id) {
        return tweetService.getUserMentions(id);
    }

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getTweetReplies(@PathVariable("id") Long id) {
        return tweetService.getTweetReplies(id);
    }

    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getTweetReposts(@PathVariable("id") Long id) {
        return tweetService.getTweetReposts(id);
    }

    @GetMapping("/{id}/likes")
    public List<UserResponseDto> likedBy(@PathVariable("id") Long id){
        return tweetService.likedBy(id);
    }

    @PostMapping("/{id}/repost")
    public TweetResponseDto repostTweet(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto){
        return tweetService.repostTweet(id, credentialsDto);
    }

    @GetMapping("/{id}/context")
    public ContextDto getContext(@PathVariable("id") Long id){
        return tweetService.getContext(id);
    }

}
