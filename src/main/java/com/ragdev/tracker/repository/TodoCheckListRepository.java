package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.TodoCheckList;
import com.ragdev.tracker.entity.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoCheckListRepository extends JpaRepository<TodoCheckList, Long> {
    boolean existsByUserIdAndTaskId(Long userId, Long taskId);
    List<TodoCheckList> findByIsChecked(boolean status);
    boolean existsByCheckDate(LocalDate today);

}
