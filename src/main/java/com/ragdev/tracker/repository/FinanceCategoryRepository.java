package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.FinanceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceCategoryRepository extends JpaRepository<FinanceCategory, Long> {

    List<FinanceCategory> findByUserId(Long userId);
}
