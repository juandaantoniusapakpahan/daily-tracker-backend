package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqTodoTaskDto;
import com.ragdev.tracker.dto.ResHabitTrackerDto;
import com.ragdev.tracker.dto.ResTodoCheckListDto;
import com.ragdev.tracker.dto.ResTodoTaskDto;
import com.ragdev.tracker.entity.TodoCheckList;
import com.ragdev.tracker.entity.TodoTask;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.exception.ResourceNotFoundException;
import com.ragdev.tracker.mapper.TodoCheckListMapper;
import com.ragdev.tracker.mapper.TodoTaskMapper;
import com.ragdev.tracker.repository.TodoCheckListRepository;
import com.ragdev.tracker.repository.TodoTaskRepository;
import com.ragdev.tracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoTaskService {

    private final TodoTaskRepository todoTaskRepository;
    private final UserRepository userRepository;
    private final TodoCheckListRepository checkListRepository;
    private static LocalDate today = LocalDate.now(ZoneId.of("Asia/Jakarta"));


    public TodoTaskService(TodoTaskRepository todoTaskRepository,
                           UserRepository userRepository,
                           TodoCheckListRepository checkListRepository) {
        this.todoTaskRepository = todoTaskRepository;
        this.userRepository = userRepository;
        this.checkListRepository = checkListRepository;
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

    public List<ResTodoTaskDto> getAll(Boolean status) {
        return todoTaskRepository.findByIsActive(status).stream()
                .map(TodoTaskMapper::toTodoResDto)
                .collect(Collectors.toList());
    }

    public ResTodoTaskDto getById(long taskId) {
        return TodoTaskMapper.toTodoResDto(
                todoTaskRepository.findById(taskId).orElseThrow(()->new ResourceNotFoundException("Task not found")));
    }

    public ResTodoTaskDto update(Long taskId, ReqTodoTaskDto dto) {
        TodoTask todoTask = todoTaskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        String newTaskName = dto.getName().trim();
        String newDescription = dto.getDescription().trim();

        if (!newTaskName.isEmpty()) {
            todoTask.setName(newTaskName);
        }
        todoTask.setDescription(newDescription);
        todoTaskRepository.save(todoTask);
        return TodoTaskMapper.toTodoResDto(todoTask);
    }

    public void delete(Long taskId) {
        TodoTask todoTask = todoTaskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        todoTask.setIsActive(false);
        todoTaskRepository.save(todoTask);
    }

    public void activate(Long taskId) {
        TodoTask todoTask = todoTaskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        todoTask.setIsActive(true);
        todoTaskRepository.save(todoTask);
    }

    public List<TodoTask> getAll() {
        return todoTaskRepository.findAll();
    }

    public List<TodoTask> getByUserId(Long userId) {
        return todoTaskRepository.findByUserId(userId);
    }

    public ResHabitTrackerDto getTodoTaskAndCheckListByMonth(Long userId) {

        ResHabitTrackerDto tracker = new ResHabitTrackerDto();
        tracker.setDates(getDatesFromTodayToMonthStart());
        tracker.setToday(today.toString());
        tracker.setMonth(today.getMonth().toString());

        List<ResTodoTaskDto> todoTaskLists = new ArrayList<>();
        List<TodoTask> todoTasks = todoTaskRepository.findByUserId(userId);

        for (TodoTask todoTask: todoTasks) {
            List<ResTodoCheckListDto> checkListDto = checkListRepository
                    .getByTaskIdAndMonth(todoTask.getId(),today.getMonthValue(), today.getYear())
                    .stream().map(TodoCheckListMapper::toDto).toList();

            ResTodoTaskDto taskDto = TodoTaskMapper.toTodoResDto(todoTask);
            taskDto.setTodoCheckLists(checkListDto);
            todoTaskLists.add(taskDto);
        }
        tracker.setTasks(todoTaskLists);
        return tracker;
    }

    public static List<String> getDatesFromTodayToMonthStart() {
        YearMonth currentMonth = YearMonth.from(today);
        List<String> dates = new ArrayList<>();

        for (int day = today.getDayOfMonth(); day >= 1; day--) {
            LocalDate date = currentMonth.atDay(day);
            dates.add(date.toString());
        }
        return dates;
    }


}
