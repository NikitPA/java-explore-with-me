package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.exception.CategoryNotFoundException;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.dto.CategoryDto;
import ru.practicum.main.model.dto.NewCategoryDto;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.service.CategoryService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        Category category = categoryRepository.save(mapper.map(categoryDto, Category.class));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public void deleteCategory(int catId) {
        Category category = findCategory(catId);
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto modificationCategory(CategoryDto categoryDto) {
        findCategory(categoryDto.getId());
        Category category = categoryRepository.save(mapper.map(categoryDto, Category.class));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> findCategories(int from, int size) {
        int page = from / size;
        Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(page, size));
        return categoryPage.stream()
                .map(category -> mapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Category findCategory(int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId));
    }

    @Override
    public List<Category> findCategoryForIds(Integer[] ids) {
        return categoryRepository.findAllById(Arrays.asList(ids));
    }

}
