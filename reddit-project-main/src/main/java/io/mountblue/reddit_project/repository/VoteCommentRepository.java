package io.mountblue.reddit_project.repository;

import io.mountblue.reddit_project.model.VoteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {
    @Query("select vc from VoteComment vc where vc.comment.id=:commentId and vc.user.id=:userId")
    public VoteComment getVoteCommentByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM VoteComment vc WHERE vc.comment.id = :commentId AND vc.user.id = :userId")
    public void deleteCommentVoteByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);
}

