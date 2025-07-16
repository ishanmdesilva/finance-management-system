package com.inova.pfms.controller;

import com.inova.pfms.dto.response.ReportDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ReportController handles requests related to generating and retrieving reports.
 * It provides endpoints for generating reports based on various parameters and
 * retrieving available report types.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Generates a report based on the provided parameters.
     *
     * @param fromDate          The start date for the report.
     * @param toDate            The end date for the report.
     * @param expense_cat_id    The ID of the expense category.
     * @param expense_cat_name  The name of the expense category.
     * @param income_source_id  The ID of the income source.
     * @param income_source_name The name of the income source.
     * @param budget_id         The ID of the budget.
     * @param budget_name       The name of the budget.
     * @param reportCode        The code of report to generate (e.g., "income", "expense", "budget").
     * @param outputFormat      The format of the output file (e.g., "pdf", "xlsx", "html").
     * @return byte[].
     */
    @GetMapping
    public ResponseEntity<byte[]> getReport(
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam String expense_cat_id,
            @RequestParam String expense_cat_name,
            @RequestParam String income_source_id,
            @RequestParam String income_source_name,
            @RequestParam String budget_id,
            @RequestParam String budget_name,
            @RequestParam String reportCode, // "income" or "expense" or "budget"
            @RequestParam String outputFormat // "pdf" or "xlsx" or "html"
    ) throws Exception {

        ReportDto reportFile = reportService.generateReport(fromDate, toDate, expense_cat_id, expense_cat_name,
                income_source_id, income_source_name, budget_id, budget_name, reportCode, outputFormat);
        String fileName = reportCode.toLowerCase() + "_report." + reportFile.getFileExtension();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                .contentType(MediaType.parseMediaType(reportFile.getContentType()))
                .body(reportFile.getFileContent());
    }

    /**
     * Retrieves all available report types.
     *
     * @return A SuccessResponseDTO containing the list of report types.
     */
    @GetMapping("/types")
    public ResponseEntity<SuccessResponseDTO> findAllReportTypes() {
        return ResponseEntity.ok(reportService.getAllReportTypes());
    }
}
