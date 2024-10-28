package com.cooksys.project1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private User author;

    @Column(nullable = false, updatable = false)
    private LocalDateTime posted = LocalDateTime.now();

    //kept as Boolean because others' implementations use it
    //should be boolean so that nulls aren't allowed
    // TODO: change to boolean
    private Boolean deleted = false;

    private String content;

    @ManyToOne
    @JoinColumn(name = "inReplyTo")
    @ToString.Exclude
    private Tweet inReplyTo;

    @ManyToOne
    @JoinColumn(name = "repostOf")
    @ToString.Exclude
    private Tweet repostOf;

    @OneToMany(mappedBy = "inReplyTo", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Tweet> replies = new ArrayList<>();

    @OneToMany(mappedBy = "repostOf", cascade = CascadeType.ALL)
    private List<Tweet> reposts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tweet_hashtags",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    @ToString.Exclude
    private List<HashTag> hashtags = new ArrayList<>();


    @ManyToMany(mappedBy = "likedTweets")
    @ToString.Exclude
    private List<User> likedByUsers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_mentions",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    private List<User> mentionedUsers = new ArrayList<>();


}
