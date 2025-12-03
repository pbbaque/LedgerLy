package com.backend.api.invoice_manager.exceptions;

public class FileUploadException extends RuntimeException {
    private final String fileName;
    private final long fileSize;

    public FileUploadException(String message) {
        super(message);
        this.fileName = null;
        this.fileSize = -1;
    }

    public FileUploadException(String message, String fileName, long fileSize) {
        super(message);
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }
}

