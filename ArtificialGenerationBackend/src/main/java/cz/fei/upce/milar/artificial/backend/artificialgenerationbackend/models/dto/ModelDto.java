package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ModelDto {
    private ModelMeshDto mesh;
    private Transform3Dto position;
    private Transform4Dto rotation;
    private Transform3Dto scale;

    @JsonGetter("mesh")
    public ModelMeshDto getMesh() {
        return mesh;
    }

    @JsonSetter("mesh")
    public void setMesh(ModelMeshDto mesh) {
        this.mesh = mesh;
    }

    @JsonGetter("position")
    public Transform3Dto getPosition() {
        return position;
    }

    @JsonSetter("position")
    public void setPosition(Transform3Dto position) {
        this.position = position;
    }

    @JsonGetter("rotation")
    public Transform4Dto getRotation() {
        return rotation;
    }

    @JsonSetter("rotation")
    public void setRotation(Transform4Dto rotation) {
        this.rotation = rotation;
    }

    @JsonGetter("scale")
    public Transform3Dto getScale() {
        return scale;
    }

    @JsonSetter("scale")
    public void setScale(Transform3Dto scale) {
        this.scale = scale;
    }
}
