package com.cooksys.project1.mappers;

import com.cooksys.project1.dtos.TweetRequestDto;
import com.cooksys.project1.dtos.TweetResponseDto;
import com.cooksys.project1.entities.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {

    TweetResponseDto entityToDto(Tweet tweet);

    Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

    List<TweetResponseDto> entitiesToDtos(List<Tweet> all);
}
