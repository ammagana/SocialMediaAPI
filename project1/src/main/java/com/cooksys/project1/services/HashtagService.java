package com.cooksys.project1.services;

import com.cooksys.project1.dtos.HashtagDto;
import com.cooksys.project1.dtos.TweetResponseDto;

import java.util.List;

public interface HashtagService {
    List<HashtagDto> getAll();
    List<TweetResponseDto> getTaggedTweets(String label);
    HashtagDto getRandom();
}
