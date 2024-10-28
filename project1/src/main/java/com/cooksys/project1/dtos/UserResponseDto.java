package com.cooksys.project1.dtos;

import com.cooksys.project1.embeddables.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class UserResponseDto {

    private String username;
    private ProfileDto profile;
    private LocalDateTime joined;

}
