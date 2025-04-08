package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.Flair;
import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.repository.FlairRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlairService {

    private final FlairRepository flairRepository;

    public FlairService(FlairRepository flairRepository) {
        this.flairRepository = flairRepository;
    }

    public void createFlair(String newSubRedditTags, SubReddit subReddit) {
        Set<Flair> flairSet = new HashSet<>();
        Set<String> newFlairs = Arrays.stream(newSubRedditTags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        for (String newFlair : newFlairs) {
            Flair flair = new Flair();
            flair.setName(newFlair);
            flair.setSubReddit(subReddit);
            flairRepository.save(flair);
            flairSet.add(flair);
        }

    }

    public Flair getFlairByName(String flair, Long subRedditId) {
        return flairRepository.fetchFlairByName(flair, subRedditId);
    }
}

