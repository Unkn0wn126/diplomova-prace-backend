package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

public class ModelDto {
    private ModelMeshDto mesh;
    private Transform3Dto position;

    @JsonGetter("Mesh")
    public ModelMeshDto getMesh() {
        return mesh;
    }

    @JsonSetter("Mesh")
    public void setMesh(ModelMeshDto mesh) {
        this.mesh = mesh;
    }

    @JsonGetter("Position")
    public Transform3Dto getPosition() {
        return position;
    }

    @JsonSetter("Position")
    public void setPosition(Transform3Dto position) {
        this.position = position;
    }
}
