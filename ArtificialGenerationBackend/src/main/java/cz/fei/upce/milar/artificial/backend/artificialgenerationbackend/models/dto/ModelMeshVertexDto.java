package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

public class ModelMeshVertexDto extends DtoAncestor {
    private Transform3Dto position;
    private Transform3Dto normal;
    private Transform2Dto texcoords;
    private Transform3Dto tangent;

    @JsonGetter("Position")
    public Transform3Dto getPosition() {
        return position;
    }

    @JsonSetter("Position")
    public void setPosition(Transform3Dto position) {
        this.position = position;
    }

    @JsonGetter("Normal")
    public Transform3Dto getNormal() {
        return normal;
    }

    @JsonSetter("Normal")
    public void setNormal(Transform3Dto normal) {
        this.normal = normal;
    }

    @JsonGetter("Texcoords")
    public Transform2Dto getTexcoords() {
        return texcoords;
    }

    @JsonSetter("Texcoords")
    public void setTexcoords(Transform2Dto texcoords) {
        this.texcoords = texcoords;
    }

    @JsonGetter("Tangent")
    public Transform3Dto getTangent() {
        return tangent;
    }

    @JsonSetter("Tangent")
    public void setTangent(Transform3Dto tangent) {
        this.tangent = tangent;
    }
}
