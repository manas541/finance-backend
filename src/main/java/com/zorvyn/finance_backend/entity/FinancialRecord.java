package com.zorvyn.finance_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FinancialRecord Entity - Money transactions
 * Income = money coming in
 * Expense = money going out
 */
@Entity
@Table(name = "financial_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Which user created this record?
     * @ManyToOne: Many records created by ONE user
     */
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    /**
     * Amount of money
     * BigDecimal: Accurate money (no rounding errors)
     * precision=19, scale=2: Can store 999999999999999.99
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    /**
     * Type of transaction
     * INCOME or EXPENSE
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    /**
     * What category?
     * SALARY, FOOD, UTILITIES, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionCategory category;

    /**
     * When did this happen?
     * LocalDate = just date, no time
     */
    @Column(nullable = false)
    private LocalDate transactionDate;

    /**
     * Extra notes about transaction
     */
    @Column(length = 500)
    private String notes;

    /**
     * When was this created?
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * When was this updated?
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Soft delete
     */
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Transaction type enum
     */
    public enum TransactionType {
        INCOME,
        EXPENSE
    }

    /**
     * Category enum
     * Different types of transactions
     */
    public enum TransactionCategory {
        SALARY,         // Income
        BONUS,          // Income
        INVESTMENT,     // Income
        GIFT,           // Income
        FOOD,           // Expense
        UTILITIES,      // Expense
        TRANSPORTATION, // Expense
        ENTERTAINMENT,  // Expense
        HEALTH,         // Expense
        SHOPPING,       // Expense
        RENT,           // Expense
        EDUCATION,      // Expense
        OTHER           // Expense
    }
}