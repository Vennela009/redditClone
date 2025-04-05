package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.*;
import io.mountblue.reddit_project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final SubRedditService subRedditService;
    private final VoteService voteService;
    private final UserService userService;
    private final FlairService flairService;
    private final VoteCommentService voteCommentService;
    private final MarkDownService markdownService;
    private final S3Service s3Service;

    @Value("${aws.s3.bucketName}")
    String bucketName;

    @Autowired
    public PostController(PostService postService, SubRedditService subRedditService, VoteService voteService, UserService userService, FlairService flairService, VoteCommentService voteCommentService, MarkDownService markdownService, S3Service s3Service) {
        this.postService = postService;
        this.subRedditService = subRedditService;
        this.voteService = voteService;
        this.userService = userService;
        this.flairService = flairService;
        this.voteCommentService = voteCommentService;
        this.markdownService=markdownService;
        this.s3Service = s3Service;
    }

    @GetMapping("/{id}")
    public String fullViewPost(@PathVariable Long id, Model model, Principal principal) {
        Post post = postService.getPostById(id);
        List<Comment> comments = post.getComments();
        post.setRelativeTime(subRedditService.calculateRelativeTime(post.getCreatedAt()));

        if (principal != null) {
            User user = userService.getUserByUsername(principal.getName());
            Vote vote = voteService.getVoteByPostAndUserId(post.getPostId(), user.getId());

            if (vote != null) {
                if (vote.getVoteType() == 1) {
                    post.setUserUpvoted(true);
                } else if (vote.getVoteType() == 0) {
                    post.setUserDownvoted(true);
                }
            }
            for (Comment comment : comments) {
                VoteComment commentVote = voteCommentService.getVoteCommentByCommentIdAndUserId(comment.getId(), user.getId());
                if (commentVote != null) {
                    if (commentVote.getVoteType() == 1) {
                        comment.setUserUpvoted(true);
                    } else if (commentVote.getVoteType() == 0) {
                        comment.setUserDownvoted(true);
                    }
                }
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("user", authentication.getPrincipal());
        }
        String htmlContent = markdownService.convertMarkdownToHtml(post.getBody());
        post.setBody(htmlContent);

        model.addAttribute("post", post);
        model.addAttribute("flairs", post.getFlairs());
        return "full-post-view";
    }


    @GetMapping("/{id}/editPost")
    public String editPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "post-updater";
    }

    @PostMapping("/{id}/updatePost")
    public String updatePost(@PathVariable Long id, @RequestParam("title") String title, @RequestParam("body") String body, @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Post post = postService.getPostById(id);
        postService.saveUpdatedPost(post, title, body, imageFile);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{postId}/voteDecider")
    public String voteDecider(@PathVariable Long postId, @RequestParam("voteType") String voteType, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getIdByUserName(username);
        voteService.voteDecider(postId, userId, voteType);
        return "redirect:/";
    }

    @PostMapping("/{postId}/voteDeciderInFullPostView")
    public String voteDeciderForFullPostView(@PathVariable Long postId, @RequestParam("voteType") String voteType, Principal principal) {
        String username = principal.getName();
        Long userId = userService.getIdByUserName(username);
        voteService.voteDecider(postId, userId, voteType);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{id}/deletePost")
    public String deletePost(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post.getMediaUrl() != null) {
            String fileKey = extractFileKeyFromUrl(post.getMediaUrl());
            s3Service.deleteFileFromS3Bucket(fileKey);
        }
        postService.deletePostById(id);
        return "redirect:/";
    }
    private String extractFileKeyFromUrl(String mediaUrl) {
        String baseUrl = String.format("https://%s.s3.amazonaws.com/", bucketName);
        if (mediaUrl.startsWith(baseUrl)) {
            return mediaUrl.replace(baseUrl, "");
        }
        return null;
    }

    @GetMapping("/new")
    public String createNewPost(Model model) {
        List<String> subRedditList = subRedditService.getAllSubRedditsByName();
        model.addAttribute("subRedditList", subRedditList);
        return "create-post";
    }

    @PostMapping("/createFirstPart")
    public String savePost(@RequestParam("title") String title,
                           @RequestParam("body") String body,
                           @RequestParam("subRedditName") String subRedditName,
                           @RequestParam("media") MultipartFile mediaFile,
                           Model model) throws IOException {
        Post post = new Post();
        post.setTitle(title);
        SubReddit subReddit = subRedditService.getSubReddit(subRedditName);
        post.setSubReddit(subReddit);
        post.setBody(body);

        System.out.println("body = " + body);

        if (!mediaFile.isEmpty()) {
            String fileUrl = s3Service.uploadFile(mediaFile, bucketName);
            post.setMediaUrl(fileUrl);

            String contentType = mediaFile.getContentType();
            post.setMediaType(contentType.startsWith("video/") ? "video" : "image");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        post.setUser(user);
        user.getPosts().add(post);
        post.setCreatedAt(LocalDateTime.now());
        postService.saveCreatePost(post);
        model.addAttribute("flairs", subReddit.getFlairs());
        model.addAttribute("selectedSubReddit", subReddit.getName());
        model.addAttribute("postId", post.getPostId());
        return "create-post-next";
    }

    @PostMapping("/createWithFlairs")
    public String savePostWithFlairs(@RequestParam("postId") Long postId, @RequestParam(value = "selectedFlairs",required = false) List<String> flairs, @RequestParam(value = "subReddit") String subRedditName) {
       if(flairs==null || flairs.isEmpty())
       {
           return "redirect:/";
       }
        Post post = postService.getPostById(postId);
        SubReddit subReddit = subRedditService.getSubReddit(subRedditName);
        Long subRedditId = subReddit.getSubRedditId();
        Set<Flair> newFlairs = new HashSet<>();
        for (String flair : flairs) {
            Flair f = flairService.getFlairByName(flair, subRedditId);
            newFlairs.add(f);
        }
        post.setFlairs(newFlairs);
        postService.saveCreatePost(post);
        return "redirect:/";
    }
    @PostMapping("/pin")
    public String setPinnedPost(@RequestParam boolean isPin , @RequestParam Long postId){
        Post post = postService.getPostById(postId);
        post.setPinned(isPin);
        postService.updateThePost(post);
        return "redirect:/";
    }
}
