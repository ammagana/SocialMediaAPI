package com.cooksys.project1.repositories;

import com.cooksys.project1.entities.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<HashTag, Long> {
    boolean existsByLabel(String tag);

    @Query(value = "select * from hash_tag order by random() limit 1", nativeQuery = true)
    HashTag findRandom();

    Optional<HashTag> findByLabel(String tag);
}
