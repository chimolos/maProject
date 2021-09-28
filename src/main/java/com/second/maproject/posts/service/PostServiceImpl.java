package com.second.maproject.posts.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.second.maproject.category.Category;
import com.second.maproject.category.CategoryRepository;
import com.second.maproject.category.CategoryService;
import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.repository.PostCommentRepository;
import com.second.maproject.posts.repository.PostRepository;
import com.second.maproject.posts.request.PostRequest;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    Cloudinary cloudinaryConfig;

    @Autowired
    PostRepository postsRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    PostCommentRepository postComRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    CategoryService categoryService;

    @Override
    public String createPost(@RequestBody PostRequest request) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);
        MultipartFile file = request.getImage();

        Post post = new Post();
        post.setUser(user);
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setPublishedDate(new Date());

        Set<String> cats = request.getAreaOfReport();
        Set<Category> categories = new HashSet<>();

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
        post.setAreaOfReport(categories);

        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                System.out.println("not a valid file");
            }
//            String oldImage = post.getImage();
            post.setImage(fileName);

            try {
                File uploadedFile = convertMultiPartToFile(file);
                Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.asMap("use_filename", true, "folder", "postUploads" + "/" + post.getUser().getId() ));
                String url = uploadResult.get("url").toString();
                post.setImagePath(url);
                postsRepo.save(post);
                return url;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
////
//            String uploadDir = "./postUploads/" + savedPost.getUser().getId();
//            Path path = Paths.get(uploadDir);
//
//            if (!Files.exists(path)) {
//                Files.createDirectories(path);
//            }
//            String uploadDirOldFile = "./postUploads/" + savedPost.getUser().getId() + File.separator + oldImage;
//            FileUploadHelper.deleteFile(uploadDirOldFile);

//            try (InputStream inputStream = file.getInputStream()) {
//                Path filePath = path.resolve(fileName);
//                System.out.println(filePath.toFile().getAbsolutePath());
//                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//            } catch (IOException e) {
//                throw new IOException("Could not save  uploaded file: " + fileName);
//            }

        } else {
                post.setImage(null);
                postsRepo.save(post);

            return "Image exists";
        }
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @Override
    public List<Post> getAllPost() {
        return postsRepo.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        Post post = postsRepo.findById(id)
                .orElseThrow(() -> new IllegalCallerException("Category with id does not exist"));
        return post;
    }

    @Override
    public List<Post> getPostsPerCategory(Long catId) {
        Category category = categoryService.getCategoryById(catId);

        return postsRepo.findByAreaOfReport(category);
    }
}
