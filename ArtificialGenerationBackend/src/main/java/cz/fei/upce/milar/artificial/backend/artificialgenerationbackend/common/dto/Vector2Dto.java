package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto;

public class Vector2Dto extends DtoAncestor {
    private float x;
    private float y;

    public Vector2Dto() {
    }

    public Vector2Dto(float x, float y) {
        this.x = x;
        this.y = y;
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
}
