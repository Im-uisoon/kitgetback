package com.project.kitgetback.service;

import com.project.kitgetback.entity.TodoEntity;
import com.project.kitgetback.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoEntity> getTodos(String email) {
        return todoRepository.findByEmail(email);
    }

    public TodoEntity addTodo(String email, String content) {
        TodoEntity todo = new TodoEntity();
        todo.setEmail(email);
        todo.setContent(content);
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id, String email) {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("할일이 없습니다"));
        if (!todo.getEmail().equals(email)) {
            throw new IllegalArgumentException("본인 할 일만 지울 수 있습니다");
        }
        todoRepository.delete(todo);
    }

    @Transactional
    public void updateCompleted(Long id, String email, boolean completed) {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일입니다"));
        if (!todo.getEmail().equals(email)) {
            throw new IllegalArgumentException("본인 할 일만 지울 수 있습니다");
        }
        todo.setCompleted(completed);
        todoRepository.save(todo);
    }
}
