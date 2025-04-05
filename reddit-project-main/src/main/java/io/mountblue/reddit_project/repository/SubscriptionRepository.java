package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.model.Subscription;
import io.mountblue.reddit_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    @Query("select s from Subscription s where s.user.id = :id and s.subReddit.subRedditId = :subRedditId")
    Subscription findBYUserAndSubReddit(@Param("id") Long id, @Param("subRedditId") Long subRedditId);

    @Query("select distinct s.subReddit.subRedditId from Subscription s where s.user.id = :id")
    List<Long> findByUserId(Long id);
}