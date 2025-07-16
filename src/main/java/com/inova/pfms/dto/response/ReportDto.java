package com.inova.pfms.dto.response;

public class ReportDto {
    private byte[] fileContent;
    private String contentType;
    private String fileExtension;

    public ReportDto(byte[] fileContent, String contentType, String fileExtension) {
        this.fileContent = fileContent;
        this.contentType = contentType;
        this.fileExtension = fileExtension;
    }

    // Getters
    public byte[] getFileContent() {
        return fileContent;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
