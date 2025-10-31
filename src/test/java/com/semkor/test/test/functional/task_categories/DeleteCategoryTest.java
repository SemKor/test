package com.semkor.test.test.functional.task_categories;

import com.semkor.test.test.models.Category;
import com.semkor.test.test.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteCategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }

    @Test
    void testDeleteCategorySuccess() {
        Category category = categoryRepository.save(new Category("категория"));

        ResponseEntity<Category> response = testRestTemplate.exchange(
                "/api/category/" + category.getId(),
                HttpMethod.DELETE,
                null,// почему null и в getAll
                Category.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(categoryRepository.existsById(category.getId()));




    }

}
