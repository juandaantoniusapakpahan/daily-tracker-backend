package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, Long> {
}
