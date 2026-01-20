package com.ragdev.tracker.repository;

import com.ragdev.tracker.dto.interf.ResTaskAverageMonthDto;
import com.ragdev.tracker.entity.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {
    List<TodoTask> findByIsActive (Boolean status);
    boolean findByIdAndIsActive(Long id, Boolean isActive);
    List<TodoTask> findByUserIdAndIsActive(Long id, boolean status);



    @Query(value = """
            SELECT tt.id AS taskId,
            tt.name AS taskName,
            ROUND((COUNT(tc.id) * 1.0 / :dayAmount) * 100.0,2) AS average
            FROM todo_tasks tt
            JOIN todo_checklists tc ON tc.task_id = tt.id
            where tt.user_id = :userId AND EXTRACT(MONTH FROM tc.check_date) = :month
            AND tt.is_active = true
            GROUP BY tt.id
            """, nativeQuery = true)
    List<ResTaskAverageMonthDto> getMonthlyTaskPrt(@Param("userId") Long userId,
                                                   @Param("month") int month,
                                                   @Param("dayAmount") int dayAmount);
}
