package com.zorvyn.finance_backend.DTO;


import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DashboardSummaryDTO - All dashboard data in one object
 * Includes totals, counts, categories, recent activity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {

    /**
     * Total income (sum of all INCOME records)
     */
    private BigDecimal totalIncome;

    /**
     * Total expenses (sum of all EXPENSE records)
     */
    private BigDecimal totalExpenses;

    /**
     * Net balance = Income - Expenses
     */
    private BigDecimal netBalance;

    /**
     * How many income transactions?
     */
    private long incomeTransactionCount;

    /**
     * How many expense transactions?
     */
    private long expenseTransactionCount;

    /**
     * Total transactions
     */
    private long totalTransactionCount;

    /**
     * Category-wise breakdown
     * Map: {SALARY: 5000, FOOD: 1200, ...}
     */
    private Map<String, BigDecimal> categoryWiseTotals;

    /**
     * How many in each category?
     * Map: {SALARY: 2, FOOD: 15, ...}
     */
    private Map<String, Long> categoryWiseCounts;

    /**
     * Last few transactions
     */
    private List<FinancialRecordDTO> recentActivity;
}
