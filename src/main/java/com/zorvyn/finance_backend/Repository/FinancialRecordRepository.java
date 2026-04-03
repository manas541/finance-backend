package com.zorvyn.finance_backend.Repository;


import com.zorvyn.finance_backend.entity.FinancialRecord;
import com.zorvyn.finance_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * FinancialRecordRepository - Database queries for records
 * Includes custom @Query for calculations
 */
@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    /**
     * Get all records for specific user (not deleted)
     */
    List<FinancialRecord> findByCreatedByUserAndIsDeletedFalse(User user);

    /**
     * Get all INCOME or EXPENSE records (not deleted)
     */
    List<FinancialRecord> findByTypeAndIsDeletedFalse(FinancialRecord.TransactionType type);

    /**
     * Get records within date range (not deleted)
     * Useful for monthly/weekly summaries
     */
    List<FinancialRecord> findByTransactionDateBetweenAndIsDeletedFalse(
            LocalDate startDate, LocalDate endDate);

    /**
     * Calculate total income or expenses
     * @Query: Custom SQL
     * SUM(fr.amount): Add all amounts
     * COALESCE: Return 0 if null (no records)
     *
     * Usage: BigDecimal total = repo.sumAmountByType(TransactionType.INCOME);
     */
    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr " +
            "WHERE fr.type = ?1 AND fr.isDeleted = false")
    BigDecimal sumAmountByType(FinancialRecord.TransactionType type);

    /**
     * Calculate total for specific user and type
     */
    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr " +
            "WHERE fr.createdByUser = ?1 AND fr.type = ?2 AND fr.isDeleted = false")
    BigDecimal sumAmountByUserAndType(User user, FinancialRecord.TransactionType type);

    /**
     * Calculate total by category
     */
    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr " +
            "WHERE fr.category = ?1 AND fr.isDeleted = false")
    BigDecimal sumAmountByCategory(FinancialRecord.TransactionCategory category);
}
