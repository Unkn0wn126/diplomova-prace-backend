package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SingleSceneDefinitionDto {
    private String sceneNamePrefix;
    private int width;
    private int height;
    private int numberOfVerticalLayers;
    private int numberOfObjects;
    private int sceneNumber;
    private boolean useSimulation;
    private int generationAlgorithm;
    private int aspectRatioId;
    @JsonSetter("sceneNamePrefix")
    public String getSceneNamePrefix() {
        return sceneNamePrefix;
    }

    public void setSceneNamePrefix(String sceneNamePrefix) {
        this.sceneNamePrefix = sceneNamePrefix;
    }

    @JsonGetter("width")
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @JsonGetter("height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @JsonGetter("numberOfVerticalLayers")
    public int getNumberOfVerticalLayers() {
        return numberOfVerticalLayers;
    }

    public void setNumberOfVerticalLayers(int numberOfVerticalLayers) {
        this.numberOfVerticalLayers = numberOfVerticalLayers;
    }

    @JsonGetter("numberOfObjects")
    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }

    @JsonGetter("sceneNumber")
    public int getSceneNumber() {
        return sceneNumber;
    }

    public void setSceneNumber(int sceneNumber) {
        this.sceneNumber = sceneNumber;
    }

    @JsonGetter("useSimulation")
    public boolean isUseSimulation() {
        return useSimulation;
    }

    public void setUseSimulation(boolean useSimulation) {
        this.useSimulation = useSimulation;
    }

    @JsonGetter("generationAlgorithm")
    public int getGenerationAlgorithm() {
        return generationAlgorithm;
    }

    public void setGenerationAlgorithm(int generationAlgorithm) {
        this.generationAlgorithm = generationAlgorithm;
    }

    @JsonGetter("aspectRatio")
    public int getAspectRatioId() {
        return aspectRatioId;
    }

    public void setAspectRatioId(int aspectRatioId) {
        this.aspectRatioId = aspectRatioId;
    }
}
