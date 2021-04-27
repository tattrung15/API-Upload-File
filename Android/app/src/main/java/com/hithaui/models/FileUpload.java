package com.hithaui.models;

public class FileUpload {
    private String fileName;

    public FileUpload() {
    }

    public FileUpload(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
