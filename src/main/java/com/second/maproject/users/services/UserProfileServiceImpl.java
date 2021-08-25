package com.second.maproject.users.services;

//import com.second.maproject.FileUploadHelper;
import com.second.maproject.FileUploadHelper;
import com.second.maproject.category.Category;
import com.second.maproject.category.CategoryRepository;
import com.second.maproject.users.requests.ProfileRequest;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserProfileServiceImpl implements UserProfileService {

//    private final String UPLOAD_DIR= new ClassPathResource()

    @Autowired
    UserRepository userRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Override
    public void updateProfile(ProfileRequest request, MultipartFile file)throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Collection<String> cats = request.getCategory();
        Collection<Category> categories = new ArrayList<>();

        if (cats == null) {
            throw new IllegalStateException("Error: Category does not exist!");
        } else {
            cats.forEach(cat -> {
                Category category = categoryRepo.findByName(cat)
                        .orElseThrow(() -> new RuntimeException("Error: Category is not found."));

//                cat.matches(String.valueOf(category));
                categories.add(category);
            });
        }

        user.setArea_of_interest(categories);

        if (!file.isEmpty()) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            String oldProfile = user.getProfilePic();
            user.setProfilePic(filename);

            User savedUser = userRepo.save(user);

            // create the path link and save to directory
            String uploadDir = "./user_profilePics/" + savedUser.getId();

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uploadDirOldFile = "./user_profilePics/" + savedUser.getId() + File.separator + oldProfile;
            if (!uploadDirOldFile.endsWith("default.png")) {
                FileUploadHelper.deleteFile(uploadDirOldFile);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(filename);
                System.out.println(filePath.toFile().getAbsolutePath());
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not save  uploaded file: " + filename);
            }
        } else {
            if (user.getProfilePic().isEmpty()) {
                user.setProfilePic(null);
                userRepo.save(user);
            }
        }
    }
}
