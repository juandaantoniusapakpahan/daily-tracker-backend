package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {
    List<TodoTask> findByIsActive (Boolean status);
    boolean findByIdAndIsActive(Long id, Boolean isActive);
}
