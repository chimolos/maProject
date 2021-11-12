package com.second.maproject.posts.request;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class PostRequest {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Set<String> areaOfReport;

    @Nullable
    private MultipartFile image;
}
