package com.ragdev.tracker.schedule;

import com.ragdev.tracker.entity.TodoCheckList;
import com.ragdev.tracker.entity.TodoTask;
import com.ragdev.tracker.entity.User;
import com.ragdev.tracker.service.CheckListService;
import com.ragdev.tracker.service.TodoTaskService;
import com.ragdev.tracker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class TodoCheckListSchedule {
    private final CheckListService checkListService;
    private final UserService userService;
    private final TodoTaskService todoTaskService;
    private final static Logger logger = LoggerFactory.getLogger(TodoCheckListSchedule.class);

    public TodoCheckListSchedule(CheckListService checkListService,
                                 UserService userService,
                                 TodoTaskService todoTaskService) {
        this.checkListService = checkListService;
        this.userService = userService;
        this.todoTaskService = todoTaskService;
    }

    @Scheduled(fixedDelay = 60_000)
    public void createCheckListDay() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Jakarta"));

        if (checkListService.existsByCheckDate(today)) {
            logger.info("Checklist already created for date {}", today);
            return;
        }

        List<User> allUser = userService.getActiveUserManual();
        for(User user : allUser) {
            List<TodoTask> allTodoTask = user.getTodoTasks();
            List<TodoCheckList> checkLists = new ArrayList<>();
            for (TodoTask task: allTodoTask){
                TodoCheckList c = new TodoCheckList();
                c.setUser(user);
                c.setTask(task);
                c.setCheckDate(today);
                c.setIsChecked(false);
                checkLists.add(c);
            }
            checkListService.saveAll(checkLists);
        }
    }
}
