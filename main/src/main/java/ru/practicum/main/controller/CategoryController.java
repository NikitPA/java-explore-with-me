package ru.practicum.main.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.dto.CategoryDto;
import ru.practicum.main.model.dto.NewCategoryDto;
import ru.practicum.main.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper mapper;

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategory(@PathVariable(name = "catId") int catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories")
    public ResponseEntity<CategoryDto> modificationCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.modificationCategory(categoryDto), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return new ResponseEntity<>(categoryService.findCategories(from, size), HttpStatus.OK);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable(name = "catId") int catId) {
        Category category = categoryService.findCategory(catId);
        return new ResponseEntity<>(mapper.map(category, CategoryDto.class), HttpStatus.OK);
    }

}
