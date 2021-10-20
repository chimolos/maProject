package com.second.maproject.likes_dislikes;

import com.second.maproject.BaseIdModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dislikes")
@Data
@NoArgsConstructor
public class Dislikes extends BaseIdModel {
    private Long postId;

    private Long thoughtId;

    private Long userId;
}
