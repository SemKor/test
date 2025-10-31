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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class GetByIdCategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }

    @Test
    void testGetByIdGetsCategoryById() {
        // создаем запись
        Category category = categoryRepository.save(new Category("категория"));
        //делаем запрос
        ResponseEntity<Category> response = restTemplate.getForEntity(
                "/api/category/list/" + category.getId(), Category.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(category.getId(), response.getBody().getId());
    }

    @Test
    void testGetByIdFailed() {

        ResponseEntity<Category> response = restTemplate.getForEntity(
                "/api/tasks/list/0", Category.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
