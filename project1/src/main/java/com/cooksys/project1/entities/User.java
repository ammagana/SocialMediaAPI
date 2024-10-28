package com.cooksys.project1.entities;


import com.cooksys.project1.embeddables.Credentials;
import com.cooksys.project1.embeddables.Profile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded // Use @Embedded if Credentials is an embeddable
    private Credentials credentials;

    private LocalDateTime joined; // Consider using LocalDateTime

    private Boolean deleted;

    @Embedded // Use @Embedded if Profile is an embeddable
    private Profile profile;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "followers_following",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers;

    @ManyToMany(mappedBy = "followers")
    private List<User> following = new ArrayList<>();

    @ManyToMany //(mappedBy = "likeByUsers")
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> likedTweets = new ArrayList<>();

    @ManyToMany(mappedBy = "mentionedUsers")
    /*@JoinTable(
            name = "user_mentions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )*/
    private List<Tweet> mentions = new ArrayList<>();





}