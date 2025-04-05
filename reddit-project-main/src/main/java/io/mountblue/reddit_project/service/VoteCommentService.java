package io.mountblue.reddit_project.service;

import io.mountblue.reddit_project.model.VoteComment;
import io.mountblue.reddit_project.repository.VoteCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteCommentService {

    private final VoteCommentRepository voteCommentRepository;

    @Autowired
    public VoteCommentService(VoteCommentRepository voteCommentRepository) {
        this.voteCommentRepository = voteCommentRepository;
    }

    public VoteComment getVoteCommentByCommentIdAndUserId(Long commentId, Long userId) {
        VoteComment voteComment = voteCommentRepository.getVoteCommentByCommentIdAndUserId(commentId, userId);
        return voteComment;
    }

    public void deleteCommentVoteByCommentIdAndUserId(Long commentId, Long userId) {
        voteCommentRepository.deleteCommentVoteByCommentIdAndUserId(commentId, userId);
    }
}
