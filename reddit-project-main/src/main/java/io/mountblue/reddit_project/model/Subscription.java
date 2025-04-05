package io.mountblue.reddit_project.model;

import jakarta.persistence.*;

@Entity
@Table(name="subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private  SubReddit subReddit;

    public Subscription() {
    }

    public Subscription(Long id, User user, SubReddit subReddit) {
        this.id = id;
        this.user = user;
        this.subReddit = subReddit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SubReddit getSubReddit() {
        return subReddit;
    }

    public void setSubReddit(SubReddit subReddit) {
        this.subReddit = subReddit;
    }
}

