package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import java.util.Base64;

public class ParseFileCommandDto {
    private String fileData;
    private String fileSuffix;

    public ParseFileCommandDto() {
    }

    public ParseFileCommandDto(String fileData, String fileSuffix) {
        this.fileData = fileData;
        this.fileSuffix = fileSuffix;
    }

    public ParseFileCommandDto(byte[] fileData, String fileSuffix) {
        this.fileData = Base64.getEncoder().encodeToString(fileData);
        this.fileSuffix = fileSuffix;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public void setFileData(byte[] fileData) {
        String fileDataAsBase64 = Base64.getEncoder().encodeToString(fileData);
        this.fileData = fileDataAsBase64;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }
}
