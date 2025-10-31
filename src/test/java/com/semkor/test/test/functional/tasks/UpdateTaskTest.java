package com.semkor.test.test.functional.tasks;

import com.semkor.test.test.models.Task;
import com.semkor.test.test.repository.TaskRepository;
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
public class UpdateTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    // как проверить частичные изменения
    void testUpdateTask() {
        Task task = taskRepository.save(new Task("дело1", true));
        Task taskData = new Task("дело2", false);

        HttpEntity<Task> requestEntity = new HttpEntity<>(taskData);
        ResponseEntity<Task> response = restTemplate.exchange(
                "/api/tasks/" + task.getId(),
                HttpMethod.PUT,
                requestEntity,
                Task.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Task taskResponse = response.getBody();
        Task afterUpdate = taskRepository.findById(response.getBody().getId()).orElseThrow();

        assertEquals(afterUpdate.getId(), response.getBody().getId());
        assertEquals("дело2", response.getBody().getTitle());
        assertFalse(response.getBody().isCompleted());
    }
}
