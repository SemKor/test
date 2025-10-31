package com.semkor.test.test.controller;

import com.semkor.test.test.models.Category;
import com.semkor.test.test.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController {


    private final CategoryRepository categoryRepository;

    public CategoryApiController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAll() {
        List<Category> category = categoryRepository.findAll();
        return ResponseEntity.ok(category);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());// 400
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category saved = categoryRepository.save(category);
        URI location = URI.create("/api/category/" + saved.getId());// что делает эта строка
        return ResponseEntity.created(location).body(saved); // 201 Created
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete (@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category newData) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(newData.getTitle());
                    Category updated = categoryRepository.save(existing);
                    return ResponseEntity.ok(updated); // 200 OK
                })
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }



}
