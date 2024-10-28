package com.cooksys.project1.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ContextDto {
    private TweetResponseDto target;
    private List<TweetResponseDto> before;
    private List<TweetResponseDto> after;
}
