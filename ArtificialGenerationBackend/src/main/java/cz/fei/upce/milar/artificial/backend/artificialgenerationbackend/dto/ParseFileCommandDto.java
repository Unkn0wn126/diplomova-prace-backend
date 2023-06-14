package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.dto;

public class ParseFileCommandDto extends DtoAncestor {
    private String fileSuffix;
    private String fileData; // byte array or base64 string instead
}
