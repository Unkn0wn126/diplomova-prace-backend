package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

public class ModelMeshVertexDto {
    private Transform3Dto position;
    private Transform3Dto normal;
    private Transform2Dto texcoords;
    private Transform3Dto tangent;

    @JsonGetter("position")
    public Transform3Dto getPosition() {
        return position;
    }

    @JsonSetter("position")
    public void setPosition(Transform3Dto position) {
        this.position = position;
    }

    @JsonGetter("normal")
    public Transform3Dto getNormal() {
        return normal;
    }

    @JsonSetter("normal")
    public void setNormal(Transform3Dto normal) {
        this.normal = normal;
    }

    @JsonGetter("texcoords")
    public Transform2Dto getTexcoords() {
        return texcoords;
    }

    @JsonSetter("texcoords")
    public void setTexcoords(Transform2Dto texcoords) {
        this.texcoords = texcoords;
    }

    @JsonGetter("tangent")
    public Transform3Dto getTangent() {
        return tangent;
    }

    @JsonSetter("tangent")
    public void setTangent(Transform3Dto tangent) {
        this.tangent = tangent;
    }
}
