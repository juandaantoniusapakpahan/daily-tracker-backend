package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ReqTodoTaskDto;
import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.security.UserDetailsImpl;
import com.ragdev.tracker.service.TodoTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo-task")
public class TodoTaskController {

    private final TodoTaskService todoTaskService;

    public TodoTaskController(TodoTaskService todoTaskService) {
        this.todoTaskService = todoTaskService;
    }

    @PostMapping("register")
    public ResponseEntity<ResApiDto<Object, Object>> registerTodoTask(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ReqTodoTaskDto dto) {
        return ResponseEntity.ok(ResApiDto.created(todoTaskService.createTodoTask(userDetails.getUser().getId(),dto)));
    }

    @PostMapping("get-all")
    public ResponseEntity<ResApiDto<Object, Object>> getAllTodoTask(@RequestParam(defaultValue = "true") Boolean status) {
        return ResponseEntity.ok(ResApiDto.ok(todoTaskService.getAll(status)));
    }

    @PostMapping("get-byId")
    public ResponseEntity<ResApiDto<Object, Object>> getById(@RequestParam Long taskId) {
        return ResponseEntity.ok(ResApiDto.ok(todoTaskService.getById(taskId)));
    }

    @PostMapping("update")
    public ResponseEntity<ResApiDto<Object, Object>> update(@RequestParam Long taskId, @Valid @RequestBody ReqTodoTaskDto dto) {
        return ResponseEntity.ok(ResApiDto.ok(todoTaskService.update(taskId, dto)));
    }

    @PostMapping("soft-delete")
    public ResponseEntity<ResApiDto<Object, Object>> delete(@RequestParam Long taskId) {
        todoTaskService.delete(taskId);
        return ResponseEntity.ok(ResApiDto.ok(null));
    }

    @PostMapping("activate")
    public ResponseEntity<ResApiDto<Object, Object>> activate(@RequestParam Long taskId) {
        todoTaskService.activate(taskId);
        return ResponseEntity.ok(ResApiDto.ok(null));
    }

    @PostMapping("get-checklist-byMonth")
    public ResponseEntity<ResApiDto<Object,Object>> getTodoTaskAndCheckList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                            @RequestParam int month,
                                                                            @RequestParam(defaultValue = "false") boolean history) {
        return ResponseEntity.ok(ResApiDto.ok(todoTaskService.getTodoTaskAndCheckListByMonth(userDetails.getUser().getId(),month, history)));
    }

    @PostMapping("get-prt-byMonth")
    public ResponseEntity<ResApiDto<Object, Object>> getMonthlyTaskPrt(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                       @RequestParam int month) {
        return ResponseEntity.ok(ResApiDto.ok(todoTaskService.getMonthlyTaskPrt(userDetails.getUser().getId(), month)));
    }
}
