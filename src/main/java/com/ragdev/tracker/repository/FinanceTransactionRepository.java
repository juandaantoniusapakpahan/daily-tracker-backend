package com.ragdev.tracker.repository;

import com.ragdev.tracker.entity.FinanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceTransactionRepository extends JpaRepository<FinanceTransaction, Long> {
}
