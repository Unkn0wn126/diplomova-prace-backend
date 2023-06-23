package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto;

import java.util.List;

public class Object3DDto extends DtoAncestor {
    private List<ObjectMeshDto> meshes;
    private Vector3Dto position;

    public List<ObjectMeshDto> getMeshes() {
        return meshes;
    }

    public void setMeshes(List<ObjectMeshDto> meshes) {
        this.meshes = meshes;
    }

    public Vector3Dto getPosition() {
        return position;
    }

    public void setPosition(Vector3Dto position) {
        this.position = position;
    }
}
