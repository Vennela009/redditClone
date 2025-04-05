package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.Vote;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.post.id = :postId AND v.user.id = :userId")
    void deleteByRowWithPostIdAndUserId(
            @Param("postId") Long postId,
            @Param("userId") Long userId
    );

    @Query("SELECT v FROM Vote v WHERE v.post.id = :postId AND v.user.id = :userId")
    Vote getVoteByPostAndUserId(
            @Param("postId") Long postId,
            @Param("userId") Long userId
    );
}
