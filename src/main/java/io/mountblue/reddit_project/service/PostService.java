package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    public PostService(PostRepository postRepository, S3Service s3Service) {
        this.postRepository = postRepository;
        this.s3Service = s3Service;
    }



    public void saveCreatePost(Post post) {
        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }


    public void saveUpdatedPost(Post post, String title, String body, MultipartFile imageFile) throws IOException {
        post.setBody(body);
        post.setTitle(title);
        if (imageFile != null && !imageFile.isEmpty()) {
           String fileUrl= s3Service.uploadFile(imageFile, bucketName);
        }
        postRepository.save(post);
    }

    public void deletePostById(Long id) {
        postRepository.deletePostById(id);
    }

    public List<Post> getSortedPosts(String sort) {
        List<Post> posts = getAllPosts();
        switch (sort) {
            case "new":
                posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
                break;
            case "old":
                posts.sort(Comparator.comparing(Post::getCreatedAt));
                break;
            case "top_week":
                filterAndSortByTimeFrame(posts,7);
                break;
            case "top_day":
                filterAndSortByTimeFrame(posts,1);
                break;
            case "top_month":
                filterAndSortByTimeFrame(posts,30);
                break;
            case "top_hour":
                filterAndSortByHour(posts);
                break;
            default:
                break;
        }
        return posts;
    }

    private void filterAndSortByTimeFrame(List<Post> posts, long days) {
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime threshold=now.minusDays(days);

        List<Post> filteredPosts=posts.stream().filter(post->post.getCreatedAt().isAfter(threshold)).collect(Collectors.toList());
        filteredPosts.sort(Comparator.comparing(Post::getTotalVotes).reversed());
        posts.clear();
        posts.addAll(filteredPosts);
    }

    public List<Post> fetchAllPostBySearch(String searchParam) {
        return postRepository.getAllPostsByRequirement(searchParam);
    }

    public List<Post> fetchAllPostBySubReddit(String subRedditName) {
        return postRepository.getAllPostBySubReddit(subRedditName);
    }
    private void filterAndSortByHour(List<Post> posts) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusHours(1);

        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getCreatedAt().isAfter(threshold))
                .collect(Collectors.toList());

        filteredPosts.sort(Comparator.comparing(Post::getTotalVotes).reversed());

        posts.clear();
        posts.addAll(filteredPosts);
    }
    public void updateThePost(Post post) {
        postRepository.save(post);
    }
    public List<Post> findSubRedditsPosts(List<Long> subReddits) {
        return postRepository.getPostsOfSubReddits(subReddits);
    }
}
