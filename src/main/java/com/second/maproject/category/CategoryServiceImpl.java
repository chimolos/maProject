package com.second.maproject.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new IllegalCallerException("Category with id does not exist"));
        return category;
    }

    @Override
    public Category createCategory(String name) {
        Category newCat = new Category();
        newCat.setName(name);
        return categoryRepo.save(newCat);
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepo.delete(category);
    }
}
