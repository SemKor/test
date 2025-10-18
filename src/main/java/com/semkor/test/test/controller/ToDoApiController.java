package com.semkor.test.test.controller;

import com.semkor.test.test.models.ToDo;
import com.semkor.test.test.repository.ToDoInMemoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ToDoApiController {

    private final ToDoInMemoryRepository toDoRepository;

    public ToDoApiController(ToDoInMemoryRepository toDoInMemoryRepository) {
        this.toDoRepository = toDoInMemoryRepository;
    }

    @GetMapping("/api/todo/list")
    public List<ToDo> list() {
        return toDoRepository.findAll();
    }
}
