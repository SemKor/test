package com.semkor.test.test;

import com.semkor.test.test.models.ToDo;
import com.semkor.test.test.repository.ToDoInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToDoEndpointTest {

    @Autowired
    private ToDoInMemoryRepository repo;

    @Autowired
    private TestRestTemplate rest;

    @BeforeEach
    void setup() {
        repo.clear();
    }

    @Test
    void testGetToDoList() {
        ResponseEntity<String> response = rest.getForEntity("/api/todo/list", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetToDoListReturnsList() {

        repo.save(new ToDo(1L, "дело1", true));
        repo.save(new ToDo(2L, "дело2", false));

        ToDo[] todos = rest.getForObject("/api/todo/list", ToDo[].class);

        assertThat(todos)
                .hasSize(2);
                //.extracting(ToDo::title)
               // .containsExactlyInAnyOrder("Купить хлеб", "Помыть кружку");
    }
}