package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.model.Vote;
import io.mountblue.reddit_project.repository.PostRepository;
import io.mountblue.reddit_project.repository.VoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, PostService postService, UserService userService, PostRepository postRepository) {
        this.voteRepository = voteRepository;
        this.postService = postService;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public void voteDecider(Long postId, Long userId, String voteType) {

        Vote existingVote = voteRepository.getVoteByPostAndUserId(postId, userId);
        Post post = postService.getPostById(postId);
        User user = userService.getUserById(userId);

        if (existingVote == null) {
            Vote newVote = new Vote();
            newVote.setPost(post);
            newVote.setUser(user);

            if (voteType.equals("up")) {
                newVote.setVoteType(1); // Upvote
                post.setTotalVotes(post.getTotalVotes() + 1);
            } else if (voteType.equals("down")) {
                newVote.setVoteType(0); // Downvote
                post.setTotalVotes(post.getTotalVotes() - 1);
            }

            voteRepository.save(newVote);
        } else {

            if (existingVote.getVoteType() == 1 && voteType.equals("down")) {

                existingVote.setVoteType(0);
                post.setTotalVotes(post.getTotalVotes() - 2);
                voteRepository.save(existingVote);
            } else if (existingVote.getVoteType() == 0 && voteType.equals("up")) {

                existingVote.setVoteType(1);
                post.setTotalVotes(post.getTotalVotes() + 2);
                voteRepository.save(existingVote);
            } else {
                voteRepository.delete(existingVote);

                if (existingVote.getVoteType() == 1) {
                    post.setTotalVotes(post.getTotalVotes() - 1);
                } else {
                    post.setTotalVotes(post.getTotalVotes() + 1);
                }
            }

            postRepository.save(post);
        }
    }

    public Vote getVoteByPostAndUserId(Long postId, Long userId) {
        Vote VoteData = voteRepository.getVoteByPostAndUserId(postId, userId);
        return VoteData;
    }


}
