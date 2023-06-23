package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto;

import java.util.List;

public class ObjectVertexDto extends DtoAncestor {
    private Vector3Dto position;
    private Vector3Dto normal;
    private List<Vector2Dto> texCoords;
    private Vector3Dto tangent;

    public Vector3Dto getPosition() {
        return position;
    }

    public void setPosition(Vector3Dto position) {
        this.position = position;
    }

    public Vector3Dto getNormal() {
        return normal;
    }

    public void setNormal(Vector3Dto normal) {
        this.normal = normal;
    }

    public List<Vector2Dto> getTexCoords() {
        return texCoords;
    }

    public void setTexCoords(List<Vector2Dto> texCoords) {
        this.texCoords = texCoords;
    }

    public Vector3Dto getTangent() {
        return tangent;
    }

    public void setTangent(Vector3Dto tangent) {
        this.tangent = tangent;
    }
}
