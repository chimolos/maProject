package com.second.maproject.thoughts_opinion;

import com.second.maproject.BaseIdModel;
import com.second.maproject.posts.models.Post;
import com.second.maproject.users.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@RequiredArgsConstructor
@Data
public class Thoughts_Opinion extends BaseIdModel {

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private int numberOfLikes;

    private int numberOfDislikes;


}
