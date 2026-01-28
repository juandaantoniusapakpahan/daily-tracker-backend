package com.ragdev.tracker.repository;

import com.ragdev.tracker.dto.interf.ResMonthlyTotalDto;
import com.ragdev.tracker.dto.interf.ResTotalFTransDto;
import com.ragdev.tracker.entity.FinanceTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanceTransactionRepository extends JpaRepository<FinanceTransaction, Long> {
    List<FinanceTransaction> findByUserId(Long id);

    @Query(value = """
               SELECT *
               FROM finance_transactions ft
               WHERE ft.user_id = :userId
                 AND ft.transaction_date BETWEEN :start AND :end
               ORDER BY ft.transaction_date DESC
            """, nativeQuery = true)
    List<FinanceTransaction> getByUserIdAndTransactionDate(@Param("userId") Long userId, @Param("start")LocalDate start,
                                        @Param("end") LocalDate end, Pageable pageable);

    @Query(value = """
               SELECT count(1)
               FROM finance_transactions ft
               WHERE ft.user_id = :userId
                 AND ft.transaction_date BETWEEN :start AND :end
            """, nativeQuery = true)
    Integer getTotalData(@Param("userId") Long userId, @Param("start")LocalDate start,
                                                           @Param("end") LocalDate end);


    @Query(value = """
            SELECT SUM(CASE WHEN ft.transaction_type = 'INCOME' THEN ft.amount ELSE 0 END) as totalIncome,
            SUM(CASE WHEN ft.transaction_type = 'EXPENSE' THEN ft.amount ELSE 0 END) as totalExpense
            FROM finance_transactions ft
            WHERE ft.user_id = :userId AND ft.transaction_date BETWEEN :start AND :end
            """, nativeQuery = true)
    ResTotalFTransDto getTotalIncomeExpense(@Param("userId") Long userId,
                                            @Param("start") LocalDate start,
                                            @Param("end") LocalDate end);

    @Query(value = """
               SELECT *
               FROM finance_transactions ft
               WHERE ft.user_id = :userId
               AND ft.transaction_type = :type
                 AND ft.transaction_date BETWEEN :start AND :end
               ORDER BY ft.transaction_date DESC
            """, nativeQuery = true)
    List<FinanceTransaction> getByUserIdAndTransactionDateAndType(@Param("userId") Long userId,
                                                                  @Param("type") String type,
                                                                  @Param("start")LocalDate start,
                                                                  @Param("end") LocalDate end,
                                                                  Pageable pageable);
    @Query(value = """
               SELECT count(1)
               FROM finance_transactions ft
               WHERE ft.user_id = :userId
               AND ft.transaction_type = :type
                 AND ft.transaction_date BETWEEN :start AND :end
            """, nativeQuery = true)
    Integer getTotalDataByType(@Param("userId") Long userId,
                                                                  @Param("type") String type,
                                                                  @Param("start")LocalDate start,
                                                                  @Param("end") LocalDate end);

    @Query(value = """
            SELECT TRIM(TO_CHAR(transaction_date, 'Month')) AS monthName,
            transaction_type as type,
            SUM(amount) as totalAmount
            FROM finance_transactions
            WHERE user_id = :userId AND EXTRACT(YEAR FROM transaction_date) = :year
            GROUP by monthName, transaction_type, EXTRACT(MONTH FROM transaction_date)
            ORDER BY
            EXTRACT(MONTH FROM transaction_date),type
            """, nativeQuery = true)
    List<ResMonthlyTotalDto> findMonthlyIncomeExpense(@Param("userId") Long userId,
                                                      @Param("year") int year);

    @Query(value = """
               SELECT *
               FROM finance_transactions ft
               WHERE ft.user_id = :userId
                 AND ft.transaction_date BETWEEN :start AND :end
               ORDER BY ft.transaction_date,ft.created_at DESC
            """, nativeQuery = true)
    List<FinanceTransaction> findByUserIdAndTransactionDate(@Param("userId") Long userId, @Param("start")LocalDate start,
                                                           @Param("end") LocalDate end);

}
