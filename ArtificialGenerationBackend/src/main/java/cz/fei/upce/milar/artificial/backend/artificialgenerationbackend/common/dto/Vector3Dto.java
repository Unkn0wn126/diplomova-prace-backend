package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto;

public class Vector3Dto extends DtoAncestor {
    private float x;
    private float y;
    private float z;

    public Vector3Dto() {
    }

    public Vector3Dto(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
