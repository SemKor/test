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
public class GetAllCategoriesTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }

    @Test
    void testGetAllCategoriesGetsListOfCategories() {
        Category category1 = categoryRepository.save(new Category("категория1"));
        Category category2 = categoryRepository.save(new Category("категория2"));

        ResponseEntity<Category[]> response = testRestTemplate.getForEntity(
                "/api/category/list", Category[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Category[] resultArray = response.getBody();
        assertNotNull(resultArray);
        assertEquals(category1.getId(), resultArray[0].getId());
        assertEquals(category2.getId(), resultArray[1].getId());


    }

}
