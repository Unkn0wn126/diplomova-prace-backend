package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

import java.util.Base64;

public class ParseFileCommandDto extends DtoAncestor {
    private String fileSuffix;
    private String fileData;

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
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
}
