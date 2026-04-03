package com.zorvyn.finance_backend.Service;


import com.zorvyn.finance_backend.entity.FinancialRecord;
import com.zorvyn.finance_backend.entity.User;
import com.zorvyn.finance_backend.Repository.FinancialRecordRepository;
import com.zorvyn.finance_backend.Repository.UserRepository;
import com.zorvyn.finance_backend.DTO.FinancialRecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FinancialRecordService - Business logic for financial records
 */
@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository financialRecordRepository;
    private final UserRepository userRepository;

    /**
     * Create new financial record
     *
     * Validations:
     * 1. Amount must be positive
     * 2. Type must be valid enum (INCOME or EXPENSE)
     * 3. Category must be valid enum
     * 4. Date cannot be in future
     * 5. User must exist
     */
    public FinancialRecord createRecord(
            Long userId,
            BigDecimal amount,
            String type,
            String category,
            LocalDate transactionDate,
            String notes) throws Exception {

        // Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Amount must be greater than 0!");
        }

        // Validate type
        FinancialRecord.TransactionType transactionType;
        try {
            transactionType = FinancialRecord.TransactionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid transaction type! Must be INCOME or EXPENSE");
        }

        // Validate category
        FinancialRecord.TransactionCategory transactionCategory;
        try {
            transactionCategory = FinancialRecord.TransactionCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid category!");
        }

        // Validate date
        if (transactionDate.isAfter(LocalDate.now())) {
            throw new Exception("Transaction date cannot be in the future!");
        }

        // Find user
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User with ID " + userId + " not found!");
        }

        // Create record
        FinancialRecord record = FinancialRecord.builder()
                .createdByUser(userOptional.get())
                .amount(amount)
                .type(transactionType)
                .category(transactionCategory)
                .transactionDate(transactionDate)
                .notes(notes)
                .isDeleted(false)
                .build();

        // Save and return
        return financialRecordRepository.save(record);
    }

    /**
     * Get all records for specific user
     */
    public List<FinancialRecord> getRecordsByUser(Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User with ID " + userId + " not found!");
        }

        User user = userOptional.get();
        return financialRecordRepository.findByCreatedByUserAndIsDeletedFalse(user);
    }

    /**
     * Get record by ID with access control
     * User can only see their own records
     */
    public Optional<FinancialRecord> getRecordById(Long recordId, Long userId) throws Exception {
        Optional<FinancialRecord> recordOptional = financialRecordRepository.findById(recordId);

        if (recordOptional.isEmpty()) {
            return Optional.empty();
        }

        FinancialRecord record = recordOptional.get();

        // Check if deleted
        if (record.getIsDeleted()) {
            return Optional.empty();
        }

        // Check access control: Only owner can see
        // (In real app, ADMIN should also see)
        if (!record.getCreatedByUser().getId().equals(userId)) {
            throw new Exception("Access denied! This record belongs to another user.");
        }

        return Optional.of(record);
    }

    /**
     * Update financial record
     */
    public FinancialRecord updateRecord(
            Long recordId,
            Long userId,
            BigDecimal amount,
            String category,
            String notes,
            LocalDate transactionDate) throws Exception {

        // Get record with access control
        Optional<FinancialRecord> recordOptional = getRecordById(recordId, userId);
        if (recordOptional.isEmpty()) {
            throw new Exception("Record not found!");
        }

        FinancialRecord record = recordOptional.get();

        // Update amount
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            record.setAmount(amount);
        }

        // Update category
        if (category != null && !category.isEmpty()) {
            try {
                record.setCategory(FinancialRecord.TransactionCategory.valueOf(category.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new Exception("Invalid category!");
            }
        }

        // Update notes
        if (notes != null) {
            record.setNotes(notes);
        }

        // Update date
        if (transactionDate != null && !transactionDate.isAfter(LocalDate.now())) {
            record.setTransactionDate(transactionDate);
        }

        // Save and return
        return financialRecordRepository.save(record);
    }

    /**
     * Soft delete record
     */
    public void softDeleteRecord(Long recordId, Long userId) throws Exception {
        Optional<FinancialRecord> recordOptional = getRecordById(recordId, userId);
        if (recordOptional.isEmpty()) {
            throw new Exception("Record not found!");
        }

        FinancialRecord record = recordOptional.get();
        record.setIsDeleted(true);
        financialRecordRepository.save(record);
    }

    /**
     * Get total income
     */
    public BigDecimal getTotalIncome() {
        return financialRecordRepository.sumAmountByType(FinancialRecord.TransactionType.INCOME);
    }

    /**
     * Get total expenses
     */
    public BigDecimal getTotalExpenses() {
        return financialRecordRepository.sumAmountByType(FinancialRecord.TransactionType.EXPENSE);
    }

    /**
     * Get net balance (income - expenses)
     */
    public BigDecimal getNetBalance() {
        return getTotalIncome().subtract(getTotalExpenses());
    }

    /**
     * Convert entity to DTO
     */
    public FinancialRecordDTO convertToDTO(FinancialRecord record) {
        return FinancialRecordDTO.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType().toString())
                .category(record.getCategory().toString())
                .transactionDate(record.getTransactionDate())
                .notes(record.getNotes())
                .createdByUserId(record.getCreatedByUser().getId())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    /**
     * Convert list to DTOs
     */
    public List<FinancialRecordDTO> convertToDTOs(List<FinancialRecord> records) {
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}