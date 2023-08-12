package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class GrabPointDto {
    private Transform3Dto localPosition;
    private Transform4Dto localRotation;
    private Transform3Dto localScale;

    @JsonGetter("localPosition")
    public Transform3Dto getLocalPosition() {
        return localPosition;
    }

    @JsonGetter("localRotation")
    public Transform4Dto getLocalRotation() {
        return localRotation;
    }

    @JsonGetter("localScale")
    public Transform3Dto getLocalScale() {
        return localScale;
    }
}
