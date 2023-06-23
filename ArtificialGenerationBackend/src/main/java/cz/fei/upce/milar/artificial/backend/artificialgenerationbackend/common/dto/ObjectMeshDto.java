package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto;

import java.util.List;

public class ObjectMeshDto extends DtoAncestor {
    private List<ObjectVertexDto> vertices;

    private List<ObjectFaceDto> faces;

    public List<ObjectVertexDto> getVertices() {
        return vertices;
    }

    public void setVertices(List<ObjectVertexDto> vertices) {
        this.vertices = vertices;
    }

    public List<ObjectFaceDto> getFaces() {
        return faces;
    }

    public void setFaces(List<ObjectFaceDto> faces) {
        this.faces = faces;
    }
}
