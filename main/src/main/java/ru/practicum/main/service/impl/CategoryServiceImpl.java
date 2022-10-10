package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.dto.CategoryDto;
import ru.practicum.main.model.dto.NewCategoryDto;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.service.CategoryService;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper mapper;

    @Transactional
    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        Category category = categoryRepository.save(mapper.map(categoryDto, Category.class));
        return mapper.map(category, CategoryDto.class);
    }

    @Transactional
    @Override
    public void deleteCategory(int catId) {
        Category category = findCategory(catId);
        categoryRepository.delete(category);
    }

    @Transactional
    @Override
    public CategoryDto modificationCategory(CategoryDto categoryDto) {
        findCategory(categoryDto.getId());
        Category category = categoryRepository.save(mapper.map(categoryDto, Category.class));
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public Page<CategoryDto> findCategories(int from, int size) {
        int page = from / size;
        return categoryRepository
                .findAll(PageRequest.of(page, size))
                .map(category -> mapper.map(category, CategoryDto.class));
    }

    @Override
    public Category findCategory(int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(
                        () -> new NotFoundException(MessageFormat.format("Category {0} not found.", catId))
                );
    }

    @Override
    public List<Category> findCategoryForIds(Integer[] ids) {
        return categoryRepository.findAllById(Arrays.asList(ids));
    }

}
