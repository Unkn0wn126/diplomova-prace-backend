package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.ParseFileCommandDto;

import java.io.Serializable;

// Annotations from here: https://stackoverflow.com/a/65685243
// TODO find better reference?
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(ParseFileCommandDto.class)
})
public abstract class DtoAncestor implements Serializable {
}
