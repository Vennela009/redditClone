package io.mountblue.reddit_project.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String comment;
    private String email;
    private int totalVotesForComments;
    private boolean userUpvoted;
    private boolean userDownvoted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;



    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteComment> voteComments;

    public int getTotalVotesForComments() {
        return totalVotesForComments;
    }

    public void setTotalVotesForComments(int totalVotesForComments) {
        this.totalVotesForComments = totalVotesForComments;
    }

    public List<VoteComment> getVoteComments() {
        return voteComments;
    }

    public void setVoteComments(List<VoteComment> voteComments) {
        this.voteComments = voteComments;
    }

    public boolean isUserUpvoted() {
        return userUpvoted;
    }

    public void setUserUpvoted(boolean userUpvoted) {
        this.userUpvoted = userUpvoted;
    }

    public boolean isUserDownvoted() {
        return userDownvoted;
    }

    public void setUserDownvoted(boolean userDownvoted) {
        this.userDownvoted = userDownvoted;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
