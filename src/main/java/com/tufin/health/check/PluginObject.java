package com.tufin.health.check;


public class PluginObject {

    private String name;
    private String fileName;
    private String description;
    private String fileType;

    public PluginObject() {
    }

    public PluginObject(String name, String fileName, String description, String fileType) {
        this.name = name;
        this.fileName = fileName;
        this.description = description;
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileType() {
        return fileType;
    }
}
