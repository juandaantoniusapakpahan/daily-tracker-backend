package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqTodoTaskDto;
import com.ragdev.tracker.dto.ResTodoTaskDto;
import com.ragdev.tracker.entity.TodoTask;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.exception.ResourceNotFoundException;
import com.ragdev.tracker.mapper.TodoTaskMapper;
import com.ragdev.tracker.repository.TodoTaskRepository;
import com.ragdev.tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoTaskService {

    private final TodoTaskRepository todoTaskRepository;
    private final UserRepository userRepository;

    public TodoTaskService(TodoTaskRepository todoTaskRepository,
                           UserRepository userRepository) {
        this.todoTaskRepository = todoTaskRepository;
        this.userRepository = userRepository;
    }

    public ResTodoTaskDto createTodoTask(Long userId, ReqTodoTaskDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TodoTask newTodoTask = new TodoTask();
        newTodoTask.setName(dto.getName());
        newTodoTask.setDescription(dto.getDescription());
        newTodoTask.setIsActive(true);
        newTodoTask.setUser(user);
        todoTaskRepository.save(newTodoTask);

        return TodoTaskMapper.toTodoResDto(newTodoTask);
    }

    public List<ResTodoTaskDto> getAll() {
        return todoTaskRepository.findAll().stream()
                .map(TodoTaskMapper::toTodoResDto)
                .collect(Collectors.toList());
    }
}
