package com.project.kitgetback.controller;

import com.project.kitgetback.entity.TodoEntity;
import com.project.kitgetback.service.TodoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin(origins = "https://im-uisoon.github.io")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Todo 리스트 불러오기
    @GetMapping
    public List<TodoEntity> getTodos(Authentication authentication) {
        String email = authentication.getName();
        return todoService.getTodos(email);
    }

    // Todo 추가
    @PostMapping
    public TodoEntity addTodo(@RequestBody TodoRequest req, Authentication authentication) {
        String email = authentication.getName();
        return todoService.addTodo(email, req.content());
    }

    // Todo 삭제
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        todoService.deleteTodo(id, email);
    }

    // Todo 완료
    @PatchMapping("/{id}/completed")
    public void updateCompleted(@PathVariable Long id, @RequestBody CompletedRequest req, Authentication authentication) {
        String email = authentication.getName();
        todoService.updateCompleted(id, email, req.completed());
    }

    public record CompletedRequest(boolean completed) {}
    public record TodoRequest(String content) {}
}
