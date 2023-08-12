package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SceneDefinitionDto {
    private String sceneNamePrefix;
    private int width;
    private int height;
    private int numberOfVerticalLayers;
    private int numberOfObjects;
    private int numberOfScenes;
    private boolean useSimulation;
    private int generationAlgorithm;
    private int aspectRatioId;
    @JsonSetter("sceneNamePrefix")
    public String getSceneNamePrefix() {
        return sceneNamePrefix;
    }

    @JsonGetter("width")
    public int getWidth() {
        return width;
    }

    @JsonGetter("height")
    public int getHeight() {
        return height;
    }

    @JsonGetter("numberOfVerticalLayers")
    public int getNumberOfVerticalLayers() {
        return numberOfVerticalLayers;
    }

    @JsonGetter("numberOfObjects")
    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    @JsonGetter("numberOfScenes")
    public int getNumberOfScenes() {
        return numberOfScenes;
    }

    @JsonGetter("useSimulation")
    public boolean isUseSimulation() {
        return useSimulation;
    }

    @JsonGetter("generationAlgorithm")
    public int getGenerationAlgorithm() {
        return generationAlgorithm;
    }

    @JsonGetter("aspectRatio")
    public int getAspectRatioId() {
        return aspectRatioId;
    }
}
