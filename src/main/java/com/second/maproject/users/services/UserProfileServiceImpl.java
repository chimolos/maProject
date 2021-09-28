package com.second.maproject.users.services;

//import com.second.maproject.FileUploadHelper;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    Cloudinary cloudinaryConfig;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Override
    public String updateProfile(ProfileRequest request, MultipartFile file)throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Collection<String> cats = request.getCategory();
        Collection<Category> categories = new ArrayList<>();

        if (cats == null) {
            throw new IllegalStateException("Error: Category does not exist!");
        } else {
            cats.forEach(cat -> {
                Category category = categoryRepo.findByNameIgnoreCase(cat)
                        .orElseThrow(() -> new RuntimeException("Error: Category is not found."));

//                cat.matches(String.valueOf(category));
                categories.add(category);
            });
        }

        user.setArea_of_interest(categories);

        if (!file.isEmpty()) {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());

//            String oldProfile = user.getProfilePic();
            user.setProfilePic(filename);

            User savedUser = userRepo.save(user);

            try {
                File uploadedFile = convertMultiPartToFile(file);
                Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.asMap("use_filename", true, "folder", "userProfilePic" + "/" + savedUser.getId() ));
                String url = uploadResult.get("url").toString();
                savedUser.setPicPath(url);
                userRepo.save(savedUser);
                return url;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // create the path link and save to directory
//            String uploadDir = "./user_profilePics/" + savedUser.getId();
//
//            Path uploadPath = Paths.get(uploadDir);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            String uploadDirOldFile = "./user_profilePics/" + savedUser.getId() + File.separator + oldProfile;
//            if (!uploadDirOldFile.endsWith("default.png")) {
//                FileUploadHelper.deleteFile(uploadDirOldFile);
//            }
//
//            try (InputStream inputStream = file.getInputStream()) {
//                Path filePath = uploadPath.resolve(filename);
//                System.out.println(filePath.toFile().getAbsolutePath());
//                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//            } catch (IOException e) {
//                throw new IOException("Could not save  uploaded file: " + filename);
//            }
        } else {
            if (user.getProfilePic().isEmpty()) {
                user.setProfilePic(null);
                userRepo.save(user);
            }
            return "Updated";
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
