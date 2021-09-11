package com.second.maproject.posts.models;

import com.second.maproject.BaseIdModel;
import com.second.maproject.category.Category;
import com.second.maproject.users.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Post extends BaseIdModel {

    @Column(nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    private String imagePath;

    private String image;

    @ManyToOne
//    @JoinColumn(name = "userid", nullable = false, updatable = false)
    private User user;

    private Date publishedDate;

    private int numberOfComments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_category",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> areaOfReport = new HashSet<>();

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
////    @JoinColumn(name = "comment_id")
//    private List<PostComment> comments = new ArrayList();


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
