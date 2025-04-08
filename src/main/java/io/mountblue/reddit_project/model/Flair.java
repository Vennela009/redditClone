package io.mountblue.reddit_project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "flair")
public class Flair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flairId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "sub_reddit")
    private SubReddit subReddit;

    public Long getFlairId() {
        return flairId;
    }

    public void setFlairId(Long flairId) {
        this.flairId = flairId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubReddit getSubReddit() {
        return subReddit;
    }

    public void setSubReddit(SubReddit subReddit) {
        this.subReddit = subReddit;
    }
}
