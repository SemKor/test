package com.semkor.test.test.functional.task_categories;

import com.semkor.test.test.models.Category;
import com.semkor.test.test.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateCategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }

    @Test
    void testUpdateCategorySuccess() {
        Category category = categoryRepository.save(new Category("категория1"));
        Category categoryData = new Category("категория2");
        HttpEntity<Category> requestData = new HttpEntity<>(categoryData);

        ResponseEntity<Category> response = testRestTemplate.exchange(
                "/api/category/" + category.getId(),
                HttpMethod.PUT,
                requestData,
                Category.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Category categoryResponse = response.getBody();
        Category afterUpdate = categoryRepository.findById(response.getBody().getId()).orElseThrow();

        assertEquals(afterUpdate.getId(), response.getBody().getId());
        assertEquals("категория2", response.getBody().getTitle());
    }
}
