package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.Comment;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.service.CommentService;
import io.mountblue.reddit_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/comments")
@Controller
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/{commentId}/voteCommentController")
    public String voteDecider(@PathVariable("commentId") Long commentId, @RequestParam("postId") Long postId, @RequestParam("voteType") String voteType, Principal principal) {
        String userName = principal.getName();
        Long userId = userService.getIdByUserName(userName);
        commentService.commentVoteDecider(commentId, userId, voteType);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/add")
    public String addComment(@RequestParam("postId") Long postId,
                             @RequestParam("comment") String comment,
                             @RequestParam(value = "parentCommentId", required = false) Long parentCommentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        String email = user.getEmail();
        commentService.saveComment(postId, username, email, comment, parentCommentId);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("{commentId}/editComment")
    public String editComment(@PathVariable Long commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("comment", comment);
        return "comment-updater";
    }

    @PostMapping("{commentId}/updateComment")
    public String updateComment(@PathVariable Long commentId, @RequestParam("comment") String comment) {
        Long postId = commentService.getPostIdByCommentId(commentId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String name = user.getUsername();
        String email = user.getEmail();
        commentService.updateComment(commentId, name, email, comment);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("{commentId}/deleteComment")
    public String deleteComment(@PathVariable Long commentId) {
        Long postId = commentService.getPostIdByCommentId(commentId);
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
