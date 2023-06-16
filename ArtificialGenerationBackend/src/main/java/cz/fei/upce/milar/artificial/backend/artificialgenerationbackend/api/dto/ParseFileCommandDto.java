package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

public class ParseFileCommandDto extends DtoAncestor {
    private String fileSuffix;
    private String fileData; // byte array or base64 string instead
}
