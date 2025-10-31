package com.semkor.test.test.functional.task_categories;

import com.semkor.test.test.models.Category;
import com.semkor.test.test.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateCategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }
// вопросы - почему именно такой url для сохранения объекта
    @Test
    void testCreateCategorySuccess() {
        Category category = categoryRepository.save(new Category("категория создана"));

        ResponseEntity<Category> response = testRestTemplate.postForEntity(
                "/api/category", category, Category.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(category.getId(), response.getBody().getId());

        boolean exists = categoryRepository.existsById(response.getBody().getId());
        assertTrue(exists, "Созданная задача должна существовать в базе данных");


    }
}
