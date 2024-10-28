package com.cooksys.project1.repositories;

import com.cooksys.project1.embeddables.Credentials;
import com.cooksys.project1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByDeletedFalse();
    Optional<User> findByCredentialsUsername(@Param("username")String username);
    Optional<User> findByCredentials(Credentials credentials);

}
