package com.second.maproject.users.services;

import com.second.maproject.users.requests.ProfileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserProfileService {
    String updateProfile(ProfileRequest request, MultipartFile file) throws IOException;
}
