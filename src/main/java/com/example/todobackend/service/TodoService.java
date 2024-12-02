package com.example.todobackend.service;

import com.example.todobackend.exceptions.ResourceNotFoundException;
import com.example.todobackend.model.Todo;
import com.example.todobackend.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoService {
    public static final String TODO_NOT_FOUND = "TODO NOT FOUND";
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo updateTodo(Integer id, Todo todo) {
        Todo existTodo = todoRepository.findById(id).orElse(null);
        if (Objects.isNull(existTodo)) {
            throw new ResourceNotFoundException(TODO_NOT_FOUND);
        }
        todo.setId(id);
        return todoRepository.save(todo);
    }

    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo deleteTodo(Integer id) {
        Todo existTodo = todoRepository.findById(id).orElse(null);
        todoRepository.deleteById(id);
        return existTodo;
    }
}
