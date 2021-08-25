package com.second.maproject.category;

import com.second.maproject.BaseIdModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseIdModel {

    @Column(name = "category_name", nullable = false)
    private String name;
}
