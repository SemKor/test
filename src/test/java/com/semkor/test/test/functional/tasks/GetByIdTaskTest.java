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
public class GetByIdTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void testGetByIdGetsTasksById() {
        Task task = taskRepository.save(new Task("дело1", true));

        ResponseEntity<Task> response = restTemplate.getForEntity(
                "/api/tasks/list/" + task.getId(), Task.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Task responseTask = response.getBody();

        assertEquals(task.getId(), responseTask.getId());
        assertEquals(task.getTitle(), responseTask.getTitle());
        assertTrue(responseTask.isCompleted());
    }

    // негативный тест - что будет если запросим id которого нет

    @Test
    void testGetByIdFailed() {

        ResponseEntity<Task> response = restTemplate.getForEntity(
                "/api/tasks/list/0", Task.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
