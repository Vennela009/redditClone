package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select Distinct u.email from User u")
    Set<String> emailSet();

    @Query("select u.username from User u")
    Set<String> userNameSet();

    Optional<User> findByUsername(String username);

    @Query("select u.id from User u where u.username=:username")
    Long getIdByUserName(@Param("username") String username);


    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);
}
