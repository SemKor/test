package com.semkor.test.test.functional.tasks;

import com.semkor.test.test.models.Task;
import com.semkor.test.test.repository.TaskRepository;
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
public class CreateTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void testCreateTaskIsCreated() {
        Task todo = new Task("дело1", true);

        ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", todo, Task.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertNotNull(response.getBody());// проверка, то тело ответа не пустое
        assertNotNull(response.getBody().getId(), "id must be generated");// проверка, что id был создан
        assertTrue(response.getBody().isCompleted());// проверка наличия корректных данных
        assertEquals("дело1", response.getBody().getTitle());

        // Проверяем, что запись реально появилась в базе данных
        boolean exists = taskRepository.existsById(response.getBody().getId());
        assertTrue(exists, "Созданная задача должна существовать в базе данных");
    }
}
