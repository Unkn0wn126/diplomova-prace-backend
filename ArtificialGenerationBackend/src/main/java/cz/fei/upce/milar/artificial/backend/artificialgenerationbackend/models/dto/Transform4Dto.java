package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

public class Transform4Dto extends DtoAncestor {
    private float x;
    private float y;
    private float z;
    private float w;

    @JsonGetter("X")
    public float getX() {
        return x;
    }

    @JsonSetter("X")
    public void setX(float x) {
        this.x = x;
    }

    @JsonGetter("Y")
    public float getY() {
        return y;
    }

    @JsonSetter("Y")
    public void setY(float y) {
        this.y = y;
    }

    @JsonGetter("Z")
    public float getZ() {
        return z;
    }

    @JsonSetter("Z")
    public void setZ(float z) {
        this.z = z;
    }

    @JsonGetter("W")
    public float getW() {
        return w;
    }

    @JsonSetter("W")
    public void setW(float w) {
        this.w = w;
    }
}
