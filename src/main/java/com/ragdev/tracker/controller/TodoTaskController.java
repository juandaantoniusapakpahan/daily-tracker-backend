package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ReqTodoTaskDto;
import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.service.TodoTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo-task")
public class TodoTaskController {

    private final TodoTaskService todoTaskService;

    public TodoTaskController(TodoTaskService todoTaskService) {
        this.todoTaskService = todoTaskService;
    }

    @PostMapping("register")
    public ResponseEntity<ResApiDto<Object, Object>> registerTodoTask(@RequestParam Long userId, @Valid @RequestBody ReqTodoTaskDto dto) {
        return ResponseEntity.ok(ResApiDto.success(todoTaskService.createTodoTask(userId,dto)));
    }

    @PostMapping("get-all")
    public ResponseEntity<ResApiDto<Object, Object>> getAllTodoTask() {
        return ResponseEntity.ok(ResApiDto.success(todoTaskService.getAll()));
    }
}
