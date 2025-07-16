package com.inova.pfms.service;

import com.inova.pfms.dto.response.ReportDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;

public interface ReportService {

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
     * @param reportType        The type of report to generate (e.g., "income", "expense", "budget").
     * @param outputFormat      The format of the output file (e.g., "pdf", "xlsx", "html").
     * @return A ReportDto containing the generated report data.
     * @throws Exception If an error occurs during report generation.
     */
    ReportDto generateReport(String fromDate, String toDate, String expense_cat_id, String expense_cat_name,
                             String income_source_id, String income_source_name, String budget_id, String budget_name,
                             String reportType, String outputFormat) throws Exception;

    /**
     * Retrieves all available report types.
     *
     * @return A SuccessResponseDTO containing the list of report types.
     */
    SuccessResponseDTO getAllReportTypes();
}
