package com.cooksys.project1.services;

import com.cooksys.project1.dtos.HashtagDto;
import com.cooksys.project1.dtos.TweetResponseDto;
import com.cooksys.project1.entities.HashTag;
import com.cooksys.project1.entities.Tweet;
import com.cooksys.project1.exceptions.NotFoundException;
import com.cooksys.project1.mappers.HashtagMapper;
import com.cooksys.project1.mappers.TweetMapper;
import com.cooksys.project1.repositories.HashtagRepository;
import com.cooksys.project1.repositories.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    @Override
    public List<HashtagDto> getAll() {
        return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }

    @Override
    public List<TweetResponseDto> getTaggedTweets(String label) {
//        System.out.println(label);
        List<Tweet> tweets = tweetRepository.findAllByLabel(label);
//        System.out.println(tweets);
//        System.out.println(tweetRepository.findById(2L));
        if(tweets.isEmpty()){
            throw new NotFoundException("Hashtag not found");
        }
        return tweetMapper.entitiesToDtos(tweets);
    }

    @Override
    public HashtagDto getRandom() {
        HashTag hashTag = hashtagRepository.findRandom();
        if(hashTag == null){
            throw new NotFoundException("Tag not found");
        }
        return hashtagMapper.entityToDto(hashTag);
    }
}
