package com.kafka.producer.utils;

import java.util.Date;

public class FileEventData {
    private String fileName;
    private String filePath;
    private String user;
    private String date;
    private String action;

    public FileEventData(String fileName, String filePath, String user, String date, String action) {
        setFileName(fileName);
        setFilePath(filePath);
        setUser(user);
        setDate(date);
        setAction(action);
    }

    public String getFileName() {
        return fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUser() {
        return user;
    }

    private void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    private void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    private void setAction(String action) {
        this.action = action;
    }
}