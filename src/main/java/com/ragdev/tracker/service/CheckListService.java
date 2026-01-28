package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqTodoCheckListDto;
import com.ragdev.tracker.dto.ResTodoCheckListDto;
import com.ragdev.tracker.entity.TodoCheckList;
import com.ragdev.tracker.entity.TodoTask;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.exception.ResourceNotFoundException;
import com.ragdev.tracker.mapper.TodoCheckListMapper;
import com.ragdev.tracker.repository.TodoCheckListRepository;
import com.ragdev.tracker.repository.TodoTaskRepository;
import com.ragdev.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CheckListService {

    private final TodoCheckListRepository todoCheckListRepository;
    private final TodoTaskRepository todoTaskRepository;
    private final UserRepository userRepository;

    public CheckListService(TodoCheckListRepository todoCheckListRepository,
                            TodoTaskRepository todoTaskRepository,
                            UserRepository userRepository) {
        this.todoCheckListRepository = todoCheckListRepository;
        this.todoTaskRepository = todoTaskRepository;
        this.userRepository = userRepository;
    }

    public ResTodoCheckListDto update(Long checkId, ReqTodoCheckListDto dto) {
        TodoCheckList todoCheckList = todoCheckListRepository.findById(checkId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo CheckList not found"));
        todoCheckList.setIsChecked(dto.getIsChecked());
        todoCheckListRepository.save(todoCheckList);
        return TodoCheckListMapper.toDto(todoCheckList);
    }

    public ResTodoCheckListDto check(Long id) {
        TodoCheckList todoCheckList = todoCheckListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo CheckList not found"));
        todoCheckList.setIsChecked(true);
        todoCheckListRepository.save(todoCheckList);
        return TodoCheckListMapper.toDto(todoCheckList);
    }

    public ResTodoCheckListDto unCheck(Long id) {
        TodoCheckList todoCheckList = todoCheckListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo CheckList not found"));
        todoCheckList.setIsChecked(false);
        todoCheckListRepository.save(todoCheckList);
        return TodoCheckListMapper.toDto(todoCheckList);
    }

    public ResTodoCheckListDto insert(Long taskId, Long userId) {
        TodoTask task = todoTaskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (todoCheckListRepository.existsByUserIdAndTaskId(userId, taskId)) {
            throw new ResourceNotFoundException("Todo CheckList already exists");
        }
        TodoCheckList newData = new TodoCheckList();
        newData.setIsChecked(true);
        newData.setCheckDate(LocalDate.now());
        newData.setTask(task);
        newData.setUser(user);
        todoCheckListRepository.save(newData);
        return TodoCheckListMapper.toDto(newData);
    }
    public List<ResTodoCheckListDto> getAll(Boolean status) {
        return todoCheckListRepository.findByIsChecked(status).stream()
                .map(TodoCheckListMapper::toDto)
                .toList();
    }

    @Transactional
    public void saveAll(List<TodoCheckList> checkLists) {
        todoCheckListRepository.saveAll(checkLists);
    }

    public boolean existsByCheckDate(LocalDate today) {
        return todoCheckListRepository.existsByCheckDate(today);
    }

    public boolean existByUserIdAndCheckDate(Long userId, LocalDate today){
        return todoCheckListRepository.existsByUserIdAndCheckDate(userId, today);
    }

}
