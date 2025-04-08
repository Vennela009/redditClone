package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.service.FlairService;
import io.mountblue.reddit_project.service.PostService;
import io.mountblue.reddit_project.service.SubRedditService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequestMapping("/sub")
@Controller
public class SubRedditController {
    private final SubRedditService subRedditService;
    private final PostService postService;
    private final FlairService flairService;

    public SubRedditController(SubRedditService subRedditService, PostService postService, FlairService flairService) {
        this.subRedditService = subRedditService;
        this.postService = postService;
        this.flairService = flairService;
    }

    @PostMapping("/new")
    public String subRedditCreate(@ModelAttribute SubReddit subReddit, @RequestParam(value = "flairsString") String flairsString, Model model) {
        subReddit.setCreatedAt(LocalDateTime.now());
        subReddit.setName("r/" + subReddit.getName());
        int randomAvatarIndex = (int) (Math.random() * 15) + 1;

        String avatarPath = "/images/avatar/" + randomAvatarIndex + ".svg";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        subReddit.setUser(user);
        subReddit.setAvatar(avatarPath);
        subRedditService.createSubReddit(subReddit);
        flairService.createFlair(flairsString, subReddit);
        return "redirect:/";
    }
}