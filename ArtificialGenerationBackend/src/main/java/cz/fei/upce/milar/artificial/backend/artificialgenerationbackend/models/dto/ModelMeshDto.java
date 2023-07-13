package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

import java.util.ArrayList;
import java.util.List;

public class ModelMeshDto extends DtoAncestor {
    private List<ModelMeshVertexDto> vertices;
    private List<ModelMeshFaceDto> faces;

    public ModelMeshDto() {
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
    }

    @JsonGetter("Vertices")
    public List<ModelMeshVertexDto> getVertices() {
        return vertices;
    }

    @JsonSetter("Vertices")
    public void setVertices(List<ModelMeshVertexDto> vertices) {
        this.vertices = vertices;
    }

    @JsonGetter("Faces")
    public List<ModelMeshFaceDto> getFaces() {
        return faces;
    }

    @JsonSetter("Faces")
    public void setFaces(List<ModelMeshFaceDto> faces) {
        this.faces = faces;
    }
}
