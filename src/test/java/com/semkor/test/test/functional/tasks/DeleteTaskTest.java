package com.semkor.test.test.functional.tasks;

import com.semkor.test.test.models.Task;
import com.semkor.test.test.repository.TaskRepository;
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
public class DeleteTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void testDeleteSuccess() {
        Task task = taskRepository.save(new Task("дело1", false));

        ResponseEntity<Task> response = restTemplate.exchange(
                "/api/tasks/" + task.getId(),
                HttpMethod.DELETE,
                null,
                Task.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // проверка, что запись отсутствует в БД
        assertFalse(taskRepository.existsById(task.getId()), "Задача не должна существовать после удаления");
    }
}