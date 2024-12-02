package com.example.todobackend.controller;

import com.example.todobackend.model.Todo;
import com.example.todobackend.service.TodoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Todo> getAllTodo(){
        return todoService.findAll();
    }
}
