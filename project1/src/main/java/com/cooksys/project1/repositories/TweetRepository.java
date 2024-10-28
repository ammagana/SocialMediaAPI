package com.cooksys.project1.repositories;

import com.cooksys.project1.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    @Query(nativeQuery = true, value = "select deleted, author_id, id, in_reply_to, posted, repost_of, content from tweet " +
            "join tweet_hashtags " +
            "on id = tweet_id " +
            "where deleted = false and hashtag_id = (select id from hash_tag where label = ?1) " +
            "order by posted desc;")
    List<Tweet> findAllByLabel(String label);

    List<Tweet> findAllByDeletedFalseOrderByPostedDesc();

    Optional<Tweet> findById(Long id);


}
