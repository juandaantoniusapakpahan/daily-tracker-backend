package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.TodoCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoCheckListRepository extends JpaRepository<TodoCheckList, Long> {
}
