package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.Transform3Dto;

import java.util.List;

public class SceneDataDto {
    private String name;
    private int width;
    private int height;
    private Transform3Dto cameraPosition;
    private List<SceneModelsCoordsDto> modelTransforms;
    private List<SceneModelsCoordsDto> grabPointTransforms;

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("width")
    public int getWidth() {
        return width;
    }

    @JsonGetter("height")
    public int getHeight() {
        return height;
    }

    @JsonGetter("cameraPosition")
    public Transform3Dto getCameraPosition() {
        return cameraPosition;
    }

    @JsonGetter("modelTransforms")
    public List<SceneModelsCoordsDto> getModelTransforms() {
        return modelTransforms;
    }

    @JsonGetter("grabPointTransforms")
    public List<SceneModelsCoordsDto> getGrabPointTransforms() {
        return grabPointTransforms;
    }
}
