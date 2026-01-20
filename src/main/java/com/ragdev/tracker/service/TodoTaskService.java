package com.ragdev.tracker.service;

import com.ragdev.tracker.dto.ReqTodoTaskDto;
import com.ragdev.tracker.dto.ResHabitTrackerDto;
import com.ragdev.tracker.dto.ResTodoCheckListDto;
import com.ragdev.tracker.dto.ResTodoTaskDto;
import com.ragdev.tracker.dto.interf.ResTaskAverageMonthDto;
import com.ragdev.tracker.entity.TodoCheckList;
import com.ragdev.tracker.entity.TodoTask;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.exception.ResourceNotFoundException;
import com.ragdev.tracker.mapper.TodoCheckListMapper;
import com.ragdev.tracker.mapper.TodoTaskMapper;
import com.ragdev.tracker.repository.TodoCheckListRepository;
import com.ragdev.tracker.repository.TodoTaskRepository;
import com.ragdev.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final static LocalDate today = LocalDate.now(ZoneId.of("Asia/Jakarta"));


    public TodoTaskService(TodoTaskRepository todoTaskRepository,
                           UserRepository userRepository,
                           TodoCheckListRepository checkListRepository) {
        this.todoTaskRepository = todoTaskRepository;
        this.userRepository = userRepository;
        this.checkListRepository = checkListRepository;
    }

    @Transactional
    public ResTodoTaskDto createTodoTask(Long userId, ReqTodoTaskDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TodoTask newTask = new TodoTask();
        newTask.setName(dto.getName());
        newTask.setDescription(dto.getDescription());
        newTask.setIsActive(true);
        newTask.setUser(user);
        todoTaskRepository.save(newTask);

        TodoCheckList newCheckList = new TodoCheckList();
        newCheckList.setUser(user);
        newCheckList.setTask(newTask);
        newCheckList.setCheckDate(today);
        newCheckList.setIsChecked(false);
        checkListRepository.save(newCheckList);

        return TodoTaskMapper.toTodoResDto(newTask);
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

    @Transactional
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

    @Transactional
    public void delete(Long taskId) {
        TodoTask todoTask = todoTaskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        todoTask.setIsActive(false);
        todoTaskRepository.save(todoTask);
    }

    @Transactional
    public void activate(Long taskId) {
        TodoTask todoTask = todoTaskRepository.findById(taskId).orElseThrow(()-> new ResourceNotFoundException("Task not found"));
        todoTask.setIsActive(true);
        todoTaskRepository.save(todoTask);
    }

    public List<TodoTask> getAll() {
        return todoTaskRepository.findAll();
    }

    public List<TodoTask> getByUserId(Long userId) {
        return todoTaskRepository.findByUserIdAndIsActive(userId,true);
    }

    public ResHabitTrackerDto getTodoTaskAndCheckListByMonth(Long userId,int month) {

        ResHabitTrackerDto tracker = new ResHabitTrackerDto();
        tracker.setDates(getDatesFromTodayToMonthStart(month));
        tracker.setToday(today.toString());
        tracker.setMonth(today.getMonth().toString());

        List<ResTodoTaskDto> todoTaskLists = new ArrayList<>();
        List<TodoTask> todoTasks = todoTaskRepository.findByUserIdAndIsActive(userId, true);

        for (TodoTask todoTask: todoTasks) {
            List<ResTodoCheckListDto> checkListDto = checkListRepository
                    .getByTaskIdAndMonth(todoTask.getId(),month, today.getYear())
                    .stream().map(TodoCheckListMapper::toDto).toList();

            ResTodoTaskDto taskDto = TodoTaskMapper.toTodoResDto(todoTask);
            taskDto.setTodoCheckLists(checkListDto);
            todoTaskLists.add(taskDto);
        }
        tracker.setTasks(todoTaskLists);
        return tracker;
    }

    public static List<String> getDatesFromTodayToMonthStart(int month) {
        YearMonth currentMonth = YearMonth.of(today.getYear(), month);
        List<String> dates = new ArrayList<>();

        for (int day = currentMonth.lengthOfMonth(); day >= 1; day--) {
            LocalDate date = currentMonth.atDay(day);
            dates.add(date.toString());
        }
        return dates;
    }

    public List<ResTaskAverageMonthDto> getMonthlyTaskPrt(Long userId, int month) {
        return todoTaskRepository.getMonthlyTaskPrt(userId, month, YearMonth.of(today.getYear(), month).lengthOfMonth());
    }
}
