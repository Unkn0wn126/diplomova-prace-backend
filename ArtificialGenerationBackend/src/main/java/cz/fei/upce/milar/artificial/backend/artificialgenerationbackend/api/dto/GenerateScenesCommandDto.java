package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.GrabPointDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.ModelDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto.SceneDefinitionDto;

import java.util.List;

public class GenerateScenesCommandDto {
    private ModelDto model;
    private List<GrabPointDto> grabPoints;
    private SceneDefinitionDto sceneDefinition;

    @JsonGetter("model")
    public ModelDto getModel() {
        return model;
    }

    @JsonGetter("grabPoints")
    public List<GrabPointDto> getGrabPoints() {
        return grabPoints;
    }

    @JsonGetter("sceneDefinition")
    public SceneDefinitionDto getSceneDefinition() {
        return sceneDefinition;
    }
}
