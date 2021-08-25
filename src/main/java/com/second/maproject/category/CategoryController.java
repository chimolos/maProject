package com.second.maproject.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/admin/category/add")
    public String createCategory(@RequestBody @Valid CategoryRequest request) {
        categoryService.createCategory(request.getName());
        return "New category created";
    }

    @PostMapping("/admin/category/update/{id}")
    public String updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequest request){
        Category category = categoryService.getCategoryById(id);

        category.setName(request.getName());
        categoryService.updateCategory(category);

        return "Category changed";
    }

    @GetMapping("/all/categories")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/all/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);

        return ResponseEntity.ok(category);
    }

    static class CategoryRequest {
        @NotNull
        @Size(min = 1, max = 100)
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

