package io.mountblue.reddit_project.service;


import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.repository.SubRedditRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubRedditService {
    private final SubRedditRepository subRedditRepository;

    public SubRedditService(SubRedditRepository subRedditRepository) {
        this.subRedditRepository = subRedditRepository;
    }

    public void createSubReddit(SubReddit sudReddit) {
        subRedditRepository.save(sudReddit);
    }


    public List<SubReddit> getAllSubReddits() {
        return subRedditRepository.findAll();
    }

    public SubReddit getSubReddit(String subredditName) {
        return subRedditRepository.findByName(subredditName);
    }

    public List<String> getAllSubRedditsByName() {
        return subRedditRepository.findAllByName();
    }

    public String calculateRelativeTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toMinutes() < 1) {
            return "Just now";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " hours ago";
        } else if (duration.toDays() == 1) {
            return "Yesterday";
        } else if (duration.toDays() < 30) {
            return duration.toDays() + " days ago";
        } else if (duration.toDays() < 365) {
            return (duration.toDays() / 30) + " months ago";
        } else {
            return (duration.toDays() / 365) + " years ago";
        }
    }
}
