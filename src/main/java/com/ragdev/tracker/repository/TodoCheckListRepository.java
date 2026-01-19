package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.TodoCheckList;
import com.ragdev.tracker.entity.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoCheckListRepository extends JpaRepository<TodoCheckList, Long> {
    boolean existsByUserIdAndTaskId(Long userId, Long taskId);
    List<TodoCheckList> findByIsChecked(boolean status);
    boolean existsByCheckDate(LocalDate today);

    @Query(value = """
            select * from todo_checklists cl
            where cl.task_id = :taskId AND EXTRACT(MONTH FROM cl.check_date) = :month
            AND EXTRACT(YEAR FROM cl.check_date) = :year
            ORDER BY cl.check_date DESC
            """,nativeQuery = true)
    List<TodoCheckList> getByTaskIdAndMonth(@Param("taskId") Long taskId,
                                            @Param("month") Integer month,
                                            @Param("year") Integer year);
}
