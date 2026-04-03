package com.zorvyn.finance_backend.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * FinancialRecordDTO - Format for API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecordDTO {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String category;
    private LocalDate transactionDate;
    private String notes;
    private Long createdByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}