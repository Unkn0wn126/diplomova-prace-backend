package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

public class Transform3Dto extends DtoAncestor {
    private float x;
    private float y;
    private float z;

    @JsonGetter("x")
    public float getX() {
        return x;
    }

    @JsonSetter("x")
    public void setX(float x) {
        this.x = x;
    }

    @JsonGetter("y")
    public float getY() {
        return y;
    }

    @JsonSetter("y")
    public void setY(float y) {
        this.y = y;
    }

    @JsonGetter("z")
    public float getZ() {
        return z;
    }

    @JsonSetter("z")
    public void setZ(float z) {
        this.z = z;
    }
}
