package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchParam, '%')) OR LOWER(p.subReddit.name) LIKE LOWER(CONCAT('%', :searchParam, '%')) OR LOWER(p.user.username) LIKE LOWER(CONCAT('%', :searchParam, '%'))")
    List<Post> getAllPostsByRequirement(@Param("searchParam") String searchParam);

    @Query("select DISTINCT p FROM Post p WHERE LOWER(p.subReddit.name) LIKE LOWER(:subRedditName)")
    List<Post> getAllPostBySubReddit(@Param("subRedditName") String subRedditName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Post p WHERE p.postId = :id")
    void deletePostById(@Param("id") Long id);

    @Query("select DISTINCT p from Post p WHERE p.subReddit.subRedditId IN :subReddits")
    List<Post> getPostsOfSubReddits(@Param("subReddits") List<Long> subReddits);

}
