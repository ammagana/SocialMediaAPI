package com.cooksys.project1.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;
    private UserResponseDto author;
    private LocalDateTime posted;
    private String content;
    private TweetResponseDto inReplyTo;
    private TweetResponseDto repostOf;
}
