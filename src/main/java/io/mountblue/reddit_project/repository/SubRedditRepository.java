package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {
    @Query("select distinct name from SubReddit ")
    List<String> findAllByName();

    @Query("select s from SubReddit s where s.name= :name")
    SubReddit findByName(@Param("name") String name);
}