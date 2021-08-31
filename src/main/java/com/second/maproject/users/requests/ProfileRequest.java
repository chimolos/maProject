package com.second.maproject.users.requests;

import java.util.Set;
public class ProfileRequest {

    private Set<String> category;

    public Set<String> getCategory() {
        return category;
    }

    public void setCategory(Set<String> category) {
        this.category = category;
    }
}
