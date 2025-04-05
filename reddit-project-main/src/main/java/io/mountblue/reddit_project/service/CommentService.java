package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.Comment;
import io.mountblue.reddit_project.model.Post;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.model.VoteComment;
import io.mountblue.reddit_project.repository.CommentRepository;
import io.mountblue.reddit_project.repository.PostRepository;
import io.mountblue.reddit_project.repository.VoteCommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final VoteCommentService voteCommentService;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, VoteCommentRepository voteCommentRepository, VoteCommentService voteCommentService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.voteCommentRepository = voteCommentRepository;
        this.voteCommentService = voteCommentService;
        this.userService = userService;
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.getReferenceById(commentId);
    }


    public void saveComment(Long postId, String name, String email, String commentContent,Long parentCommentId) {
        Post post = postRepository.getReferenceById(postId);
        Comment comment = new Comment();
        comment.setComment(commentContent);
        comment.setEmail(email);
        comment.setName(name);
        comment.setPost(post);


        if (parentCommentId != null) {
            Comment parentComment = commentRepository.getReferenceById(parentCommentId);
            comment.setParentComment(parentComment);
            parentComment.getReplies().add(comment);
        }
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, String name, String email, String commentContent) {
        Comment comment = commentRepository.getReferenceById(commentId);
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(commentContent);
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Long getPostIdByCommentId(Long commentId) {
        Comment comment = commentRepository.getReferenceById(commentId);
        Post post = comment.getPost();
        return post.getPostId();
    }

    public void commentVoteDecider(Long commentId, Long userId, String voteType) {
        VoteComment voteComment = voteCommentService.getVoteCommentByCommentIdAndUserId(commentId, userId);
        User user = userService.getUserById(userId);
        Comment comment = commentRepository.getReferenceById(commentId);
        if (voteComment == null) {
            VoteComment newVoteComment = new VoteComment();

            if (voteType.equals("up")) {
                int totalVotes = comment.getTotalVotesForComments();
                comment.setTotalVotesForComments(totalVotes + 1);
                newVoteComment.setVoteType(1);
            } else if (voteType.equals("down")) {
                int totalVotes = comment.getTotalVotesForComments();
                comment.setTotalVotesForComments(totalVotes - 1);
                newVoteComment.setVoteType(0);
            }
            newVoteComment.setUser(user);
            newVoteComment.setComment(comment);
            commentRepository.save(comment);
            voteCommentRepository.save(newVoteComment);
        } else {
            int voteCommentType = voteComment.getVoteType();

            if (voteCommentType == 1 && voteType.equals("up")) {
                int totalVotes = comment.getTotalVotesForComments();
                totalVotes -= 1;
                voteCommentService.deleteCommentVoteByCommentIdAndUserId(commentId, userId);
                comment.setTotalVotesForComments(totalVotes);
            } else if (voteCommentType == 1 && voteType.equals("down")) {
                int totalVotes = comment.getTotalVotesForComments();
                totalVotes -= 2;
                voteComment.setVoteType(0);
                voteCommentRepository.save(voteComment);
                comment.setTotalVotesForComments(totalVotes);
            } else if (voteCommentType == 0 && voteType.equals("down")) {
                int totalVotes = comment.getTotalVotesForComments();
                totalVotes += 1;
                voteCommentService.deleteCommentVoteByCommentIdAndUserId(commentId, userId);
                comment.setTotalVotesForComments(totalVotes);
            } else if (voteCommentType == 0 && voteType.equals("up")) {
                int totalVotes = comment.getTotalVotesForComments();
                totalVotes += 2;
                voteComment.setVoteType(1);
                voteCommentRepository.save(voteComment);
                comment.setTotalVotesForComments(totalVotes);
            }
            commentRepository.save(comment);
        }
    }
}
