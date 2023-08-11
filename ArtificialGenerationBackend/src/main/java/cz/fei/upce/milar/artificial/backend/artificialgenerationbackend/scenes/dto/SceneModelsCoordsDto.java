package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.Transform3Dto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.Transform4Dto;

public class SceneModelsCoordsDto {
    private Transform3Dto position;
    private Transform4Dto rotation;

    @JsonGetter("position")
    public Transform3Dto getPosition() {
        return position;
    }

    @JsonGetter("rotation")
    public Transform4Dto getRotation() {
        return rotation;
    }
}
