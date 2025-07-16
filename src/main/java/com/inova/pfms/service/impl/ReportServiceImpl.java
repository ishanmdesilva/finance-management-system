package com.inova.pfms.service.impl;

import com.inova.pfms.dto.response.ReportDto;
import com.inova.pfms.dto.response.ReportTypeResponseDto;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.entity.User;
import com.inova.pfms.enums.ReportTypeEnum;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.service.ReportService;
import com.inova.pfms.util.CommonMethod;
import com.inova.pfms.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inova.pfms.constants.LogMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final UserUtil userUtil;
    @Autowired
    private DataSource dataSource;

    @Override
    public ReportDto generateReport(String fromDate, String toDate, String expense_cat_id, String expense_cat_name,
                                    String income_source_id, String income_source_name, String budget_id, String budget_name,
                                    String reportType, String outputFormat) throws Exception {
        User loggedInUser = userUtil.getLoggedInUser();
        Map<String, Object> params = null;
        if (ReportTypeEnum.INCOME.getCode().equalsIgnoreCase(reportType)) {
            params = prepareReportParams(fromDate, toDate, income_source_id, income_source_name, reportType, loggedInUser);
        } else if (ReportTypeEnum.EXPENSE.getCode().equalsIgnoreCase(reportType)) {
            params = prepareReportParams(fromDate, toDate, expense_cat_id, expense_cat_name, reportType, loggedInUser);
        } else if (ReportTypeEnum.BUDGET.getCode().equalsIgnoreCase(reportType)) {
            params = prepareReportParams(fromDate, toDate, budget_id, budget_name, reportType, loggedInUser);
            params.put("expense_category_id", isValidCategory(expense_cat_id) ? expense_cat_id : null);
            params.put("expense_category", isValidCategory(expense_cat_name) ? expense_cat_name : null);
        }else {
            throw new IllegalArgumentException("Invalid report type: " + reportType);
        }

        System.out.println("Report parameters: " + params);

        try (InputStream reportStream = getClass().getResourceAsStream(getReportPath(reportType))) {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            try (Connection conn = dataSource.getConnection()) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
                return exportReport(jasperPrint, outputFormat);
            }
        }
    }

    @Override
    public SuccessResponseDTO getAllReportTypes() {
        try {
            log.info(GET_ALL_REPORT_TYPES_CALLED);
            List<ReportTypeResponseDto> responseList = Arrays.stream(ReportTypeEnum.values())
                    .map(type -> new ReportTypeResponseDto(type.getName(), type.getCode()))
                    .toList();

            log.info(GET_ALL_EVENT_TYPES_COMPLETED);

            return CommonMethod.getSuccessResponse(
                    "Report types retrieved successfully",
                    responseList
            );
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find report types ", e.getMessage());
        }
    }

    private Map<String, Object> prepareReportParams(String fromDate, String toDate, String category, String cat_name, String reportCode, User user) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> params = new HashMap<>();
        params.put("from_date", new java.sql.Date(formatter.parse(fromDate).getTime()));
        params.put("to_date", new java.sql.Date(formatter.parse(toDate).getTime()));
        params.put("user_id", user.getId());

        if (ReportTypeEnum.INCOME.getCode().equalsIgnoreCase(reportCode)) {
            params.put("income_source_id", isValidCategory(category) ? category : null);
            params.put("income_source_name", isValidCategoryName(cat_name) ? cat_name : null);
        } else if (ReportTypeEnum.EXPENSE.getCode().equalsIgnoreCase(reportCode)) {
            params.put("expense_category_id", isValidCategory(category) ? category : null);
            params.put("expense_cat_name", isValidCategory(cat_name) ? cat_name : null);
        } else if (ReportTypeEnum.BUDGET.getCode().equalsIgnoreCase(reportCode)) {
            params.put("user", user.getFirstName() + " " + user.getLastName());
            params.put("budget_id", isValidCategory(category) ? category : null);
            params.put("budget", isValidCategoryName(cat_name) ? cat_name : null);
        }else {
            throw new IllegalArgumentException("Invalid report type: " + reportCode);
        }
        return params;
    }

    private String getReportPath(String reportCode) {
        if (ReportTypeEnum.INCOME.getCode().equalsIgnoreCase(reportCode)) {
            return "/templates/reports/ipay_income.jrxml";
        } else if (ReportTypeEnum.EXPENSE.getCode().equalsIgnoreCase(reportCode)) {
            return "/templates/reports/ipay_expense.jrxml";
        } else if (ReportTypeEnum.BUDGET.getCode().equalsIgnoreCase(reportCode)) {
            return "/templates/reports/users_budget_category.jrxml";
        }else {
            throw new IllegalArgumentException("Invalid report type: " + reportCode);
        }
    }

    private boolean isValidCategory(String category) {
        return category != null && !category.isEmpty();
    }

    private boolean isValidCategoryName(String categoryName) {
        return categoryName != null && !categoryName.isEmpty();
    }

    private ReportDto exportReport(JasperPrint jasperPrint, String outputFormat) throws JRException {
        switch (outputFormat.toLowerCase()) {
            case "pdf":
                return new ReportDto(JasperExportManager.exportReportToPdf(jasperPrint), MediaType.APPLICATION_PDF_VALUE, "pdf");
            case "xlsx":
                return new ReportDto(exportToExcel(jasperPrint),
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
            case "html":
                return new ReportDto(exportToHtml(jasperPrint), MediaType.TEXT_HTML_VALUE, "html");
            default:
                throw new IllegalArgumentException("Unsupported output format: " + outputFormat);
        }
    }

    private byte[] exportToExcel(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JRXlsxExporter exporter = new JRXlsxExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setCollapseRowSpan(false);

            exporter.setConfiguration(configuration);
            exporter.exportReport();

            return outputStream.toByteArray();
        } catch (IOException | JRException e) {
            throw new RuntimeException("Error exporting report to Excel", e);
        }
    }

    private byte[] exportToHtml(JasperPrint jasperPrint) throws JRException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlExporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
            exporter.exportReport();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error exporting report to HTML", e);
        }
    }
}