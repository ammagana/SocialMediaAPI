package com.cooksys.project1.controllers;

import com.cooksys.project1.dtos.HashtagDto;
import com.cooksys.project1.dtos.TweetResponseDto;
import com.cooksys.project1.services.HashtagService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
    private final HashtagService hashtagService;

    @GetMapping
    public List<HashtagDto> getTags(){
        return hashtagService.getAll();
    }

    @GetMapping("/{label}")
    public List<TweetResponseDto> getTaggedTweets(@PathVariable("label") String label){
        return hashtagService.getTaggedTweets(label);
    }

    @GetMapping("/randomTag")
    public HashtagDto getRandomTag(){
        return hashtagService.getRandom();
    }
}
