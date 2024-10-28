package com.cooksys.project1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class HashTag {
    @Id
    @GeneratedValue
    private Long id;

    private String label;

    private Timestamp firstUsed;

    private Timestamp lastUsed;

    @ManyToMany(mappedBy = "hashtags")
    /*@JoinTable(
            name = "tweet_hashtags",
            joinColumns = @JoinColumn(name = "hashtag_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )*/
    List<Tweet> tweets = new ArrayList<>();

}
