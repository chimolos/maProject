package com.second.maproject.category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category createCategory(String name);
    void updateCategory(Category category);
    void deleteCategory(Category category);
}
