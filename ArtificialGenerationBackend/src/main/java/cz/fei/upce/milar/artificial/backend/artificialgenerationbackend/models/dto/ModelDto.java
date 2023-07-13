package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

public class ModelDto extends DtoAncestor {
    private ModelMeshDto mesh;
    private Transform3Dto position;

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
}
