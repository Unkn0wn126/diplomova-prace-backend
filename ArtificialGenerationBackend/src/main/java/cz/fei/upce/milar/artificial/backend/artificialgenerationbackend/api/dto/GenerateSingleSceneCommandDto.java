package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.GrabPointDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.ModelDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto.SingleSceneDefinitionDto;

import java.util.List;

public class GenerateSingleSceneCommandDto {
    private ModelDto model;
    private List<GrabPointDto> grabPoints;
    private SingleSceneDefinitionDto sceneDefinition;

    @JsonGetter("model")
    public ModelDto getModel() {
        return model;
    }

    @JsonSetter("model")
    public void setModel(ModelDto model) {
        this.model = model;
    }

    @JsonGetter("grabPoints")
    public List<GrabPointDto> getGrabPoints() {
        return grabPoints;
    }

    @JsonSetter("grabPoints")
    public void setGrabPoints(List<GrabPointDto> grabPoints) {
        this.grabPoints = grabPoints;
    }

    @JsonGetter("sceneDefinition")
    public SingleSceneDefinitionDto getSceneDefinition() {
        return sceneDefinition;
    }

    @JsonSetter("sceneDefinition")
    public void setSceneDefinition(SingleSceneDefinitionDto sceneDefinition) {
        this.sceneDefinition = sceneDefinition;
    }
}
