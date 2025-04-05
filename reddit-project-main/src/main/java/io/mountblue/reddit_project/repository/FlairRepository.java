package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.Flair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlairRepository extends JpaRepository<Flair, Long> {
    @Query("SELECT f from Flair f where f.name=:flairName and f.subReddit.subRedditId=:subRedditId")
    Flair fetchFlairByName(@Param("flairName") String flair, Long subRedditId);
}
