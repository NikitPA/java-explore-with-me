package ru.practicum.main.service;

import org.springframework.data.domain.Page;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.dto.CategoryDto;
import ru.practicum.main.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    void deleteCategory(int catId);

    CategoryDto modificationCategory(CategoryDto categoryDto);

    Page<CategoryDto> findCategories(int from, int size);

    Category findCategory(int catId);

    List<Category> findCategoryForIds(Integer[] ids);
}
