package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.FinanceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceCategoryRepository extends JpaRepository<FinanceCategory, Long> {
}
