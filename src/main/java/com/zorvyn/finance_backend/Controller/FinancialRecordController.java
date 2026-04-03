package com.zorvyn.finance_backend.Controller;

import com.zorvyn.finance_backend.entity.FinancialRecord;
import com.zorvyn.finance_backend.entity.User;
import com.zorvyn.finance_backend.Service.FinancialRecordService;
import com.zorvyn.finance_backend.Service.UserService;
import com.zorvyn.finance_backend.DTO.FinancialRecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * FinancialRecordController - REST API endpoints for financial records
 */
@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;
    private final UserService userService;

    /**
     * GET /api/v1/records?userId=1
     * Get all records for current user
     *
     * @RequestParam Long userId: Get from query parameter
     * Example: GET /api/v1/records?userId=1
     */
    @GetMapping
    public ResponseEntity<?> getMyRecords(@RequestParam Long userId) {
        try {
            // Get current user
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            // Get records
            List<FinancialRecord> records = financialRecordService.getRecordsByUser(userId);
            List<FinancialRecordDTO> dtos = financialRecordService.convertToDTOs(records);

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /api/v1/records/{id}?userId=1
     * Get specific record
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecordById(
            @PathVariable("id") Long recordId,
            @RequestParam Long userId) {
        try {
            Optional<FinancialRecord> record = financialRecordService.getRecordById(recordId, userId);

            if (record.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Record not found");
            }

            FinancialRecordDTO dto = financialRecordService.convertToDTO(record.get());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * POST /api/v1/records?userId=1
     * Create new financial record
     *
     * Request body:
     * {
     *     "amount": 5000.00,
     *     "type": "INCOME",
     *     "category": "SALARY",
     *     "transactionDate": "2024-01-15",
     *     "notes": "Monthly salary"
     * }
     */
    @PostMapping
    public ResponseEntity<?> createRecord(
            @RequestParam Long userId,
            @RequestBody FinancialRecord record) {
        try {
            // Get user
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            // Create record
            FinancialRecord createdRecord = financialRecordService.createRecord(
                    userId,
                    record.getAmount(),
                    record.getType().toString(),
                    record.getCategory().toString(),
                    record.getTransactionDate(),
                    record.getNotes()
            );

            FinancialRecordDTO dto = financialRecordService.convertToDTO(createdRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating record: " + e.getMessage());
        }
    }

    /**
     * PUT /api/v1/records/{id}?userId=1
     * Update record
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(
            @PathVariable("id") Long recordId,
            @RequestParam Long userId,
            @RequestBody FinancialRecord updatedRecord) {
        try {
            FinancialRecord updated = financialRecordService.updateRecord(
                    recordId,
                    userId,
                    updatedRecord.getAmount(),
                    updatedRecord.getCategory() != null ? updatedRecord.getCategory().toString() : null,
                    updatedRecord.getNotes(),
                    updatedRecord.getTransactionDate()
            );

            FinancialRecordDTO dto = financialRecordService.convertToDTO(updated);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating record: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/v1/records/{id}?userId=1
     * Delete record
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(
            @PathVariable("id") Long recordId,
            @RequestParam Long userId) {
        try {
            financialRecordService.softDeleteRecord(recordId, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * GET /api/v1/records/dashboard/summary?userId=1
     * Get dashboard summary
     */
    @GetMapping("/dashboard/summary")
    public ResponseEntity<?> getDashboardSummary(@RequestParam Long userId) {
        try {
            // Get user
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            // Get analytics
            BigDecimal totalIncome = financialRecordService.getTotalIncome();
            BigDecimal totalExpenses = financialRecordService.getTotalExpenses();
            BigDecimal netBalance = financialRecordService.getNetBalance();

            // Return as string (simplified version)
            String summary = "Dashboard Summary:\n" +
                    "Total Income: " + totalIncome + "\n" +
                    "Total Expenses: " + totalExpenses + "\n" +
                    "Net Balance: " + netBalance;

            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}
