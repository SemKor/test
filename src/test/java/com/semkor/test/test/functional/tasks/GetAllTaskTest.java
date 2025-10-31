package com.semkor.test.test.functional.tasks;

import com.semkor.test.test.models.Task;
import com.semkor.test.test.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetAllTaskTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void testGetAllSuccess() {
        Task task1 = taskRepository.save(new Task("дело1", true));
        Task task2 = taskRepository.save(new Task("дело2", true));

        ResponseEntity<Task[]> response = testRestTemplate.getForEntity("/api/tasks/list", Task[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Task[] resultArray = response.getBody();

        assertNotNull(resultArray, "Ответ не должен быть null");
        assertEquals(2, resultArray.length);
        assertEquals(task1.getId(), resultArray[0].getId());
        assertEquals(task2.getId(), resultArray[1].getId());
    }

    @Test
    void testGetAllGetEmptyList() {
        ResponseEntity<Task[]> response = testRestTemplate.getForEntity("/api/tasks/list", Task[].class);

        assertNotNull(response.getBody(), "Ответ не должен быть null");

        Task[] resultArray = response.getBody();
        assertArrayEquals(new Task[0], resultArray, "Список задач должен быть пуст");
    }

    @Test
    void testGetAllWithTitleFilter() {
        Task task1 = taskRepository.save(new Task("дело1", true));
        Task task2 = taskRepository.save(new Task("дело2", true));
        Task task3 = taskRepository.save(new Task("новое", true));
        Task task4 = taskRepository.save(new Task("дело3", true));

        ResponseEntity<Task[]> response = testRestTemplate.getForEntity("/api/tasks/list?title=дел", Task[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body is null");

        Task[] filtersTasks = response.getBody();
        assertEquals(3, filtersTasks.length);
        assertEquals(task1.getId(), filtersTasks[0].getId());
        assertEquals(task2.getId(), filtersTasks[1].getId());
        assertEquals(task4.getId(), filtersTasks[2].getId());

        List<Task> filterdList = List.of(response.getBody());
        assertTrue(filterdList.stream().allMatch(t -> t.getTitle().toLowerCase().contains("дел")));
    }

    @Test
    void testGetAllWithCompletedFilter() {
        Task task1 = taskRepository.save(new Task("дело1", false));
        Task task2 = taskRepository.save(new Task("дело2", true));
        Task task3  = taskRepository.save(new Task("новое", false));
        Task task4  = taskRepository.save(new Task("новое", true));

        ResponseEntity<Task[]> response1 = testRestTemplate.getForEntity("/api/tasks/list?completed=true",Task[].class);
        ResponseEntity<Task[]> response2 = testRestTemplate.exchange(
                "/api/tasks/list?completed=true",
                HttpMethod.GET,
                null,
                Task[].class);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody(), "Body should not be empty");

        //List<Task> filteredTask = List.of(response2.getBody());
        List<Task> filteredTask = Arrays.asList(response2.getBody());
        System.out.println(filteredTask);
        System.out.println(filteredTask.size());
        assertEquals(2,filteredTask.size());
        assertEquals(task2.getId(),filteredTask.get(0).getId());
        assertEquals(task4.getId(),filteredTask.get(1).getId());
    }

    @Test
    void testGetAllWithCompletedAndTitleFilter() {
        Task task1 = taskRepository.save(new Task("дело1", false));
        Task task2 = taskRepository.save(new Task("дело2", true));
        Task task3 = taskRepository.save(new Task("дело3", false));
        Task task4 = taskRepository.save(new Task("новое1", true));
        Task task5 = taskRepository.save(new Task("новое2", false));
        Task task6 = taskRepository.save(new Task("новое3", true));

        ResponseEntity<List<Task>> response = testRestTemplate.exchange(
                "/api/tasks/list?title=нов&completed=true",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Task>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<Task> filtered = response.getBody();
        assertEquals(2, filtered.size());
        assertEquals(task4.getId(), filtered.get(0).getId());
        assertEquals(task6.getId(), filtered.get(1).getId());
        assertTrue(filtered.stream().allMatch(el -> el.getTitle().toLowerCase().contains("нов")));
    }

    @Test
    void testGetAllWithEmptyQuery() {
        Task task1 = taskRepository.save(new Task("дело1", false));
        Task task2 = taskRepository.save(new Task("дело2", true));
        Task task3 = taskRepository.save(new Task("дело3", false));
        Task task4 = taskRepository.save(new Task("новое1", true));
        Task task5 = taskRepository.save(new Task("новое2", false));
        Task task6 = taskRepository.save(new Task("новое3", true));

        ResponseEntity<List<Task>> response = testRestTemplate.exchange(
                "/api/tasks/list?title=",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Task>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<Task> filtered = response.getBody();
        assertEquals(6, filtered.size());

    }

    @Test
    void testGetAllWithQueryContainsDigits() {
        Task task1 = taskRepository.save(new Task("дело1", false));
        Task task2 = taskRepository.save(new Task("дело2", true));
        Task task3 = taskRepository.save(new Task("дело3", false));
        Task task4 = taskRepository.save(new Task("новое1", true));
        Task task5 = taskRepository.save(new Task("новое2", false));
        Task task6 = taskRepository.save(new Task("новое3", true));

        ResponseEntity<List<Task>> response = testRestTemplate.exchange(
                "/api/tasks/list?title=111",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Task>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<Task> filtered = response.getBody();
        assertEquals(0, filtered.size());
    }

    @Test
    void testGetAllWithNumericTitleShouldFail() {
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/tasks/list?title=111",
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println(response.getBody());
        assertTrue(response.getBody().contains("Title must contain at least one non-digit character"));
    }









}
