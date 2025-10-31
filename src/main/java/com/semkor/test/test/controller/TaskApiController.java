package com.semkor.test.test.controller;

import com.semkor.test.test.models.Task;
import com.semkor.test.test.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskApiController {

    private final TaskRepository taskRepository;

    public TaskApiController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 🟡 READ (все)
    @GetMapping("/list")
    public ResponseEntity<List<Task>> getAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean completed
    ) {
        List<Task> tasks;

        if (title != null && completed != null) {
            tasks = taskRepository.findByTitleContainingIgnoreCaseAndCompleted(title, completed);
        }
        else if (title != null) {
            tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        }
        else if (completed != null) {
            tasks = taskRepository.findByCompleted(completed);
        }
        else {
            tasks = taskRepository.findAll();
        }
        return ResponseEntity.ok(tasks);
    }

    // 🟡 READ (по id)
    @GetMapping("/list/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());// 400
    }


    // 🟢 CREATE
    @PostMapping()
    public ResponseEntity<Task> create(@RequestBody Task todo) {
        Task saved = taskRepository.save(todo);
        URI location = URI.create("/api/todo/" + saved.getId());
        return ResponseEntity.created(location).body(saved); // 201 Created
    }

//    // 🔹 Создать задачу
//    @PostMapping
//    public ResponseEntity<ToDo> create(@RequestBody ToDo todo) {
//        ToDo saved = toDoRepository.save(todo);
//        return ResponseEntity.ok(saved);
//    }

    // 🟠 UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task newData) {
        return taskRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(newData.getTitle());
                    existing.setCompleted(newData.isCompleted());
                    Task updated = taskRepository.save(existing);
                    return ResponseEntity.ok(updated); // 200 OK
                })
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    // 🔴 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}