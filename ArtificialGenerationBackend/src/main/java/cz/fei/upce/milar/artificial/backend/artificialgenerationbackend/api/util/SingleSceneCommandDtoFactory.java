package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.util;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GenerateScenesCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GenerateSingleSceneCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto.SceneDefinitionDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto.SingleSceneDefinitionDto;

public class SingleSceneCommandDtoFactory {

    public static GenerateSingleSceneCommandDto createFromGenerateScenesCommandDto(GenerateScenesCommandDto source,
                                                                                   int index) {
        GenerateSingleSceneCommandDto result = new GenerateSingleSceneCommandDto();
        result.setModel(source.getModel());
        result.setGrabPoints(source.getGrabPoints());
        SingleSceneDefinitionDto singleSceneDefinitionDto =
                createFromSceneDefinitionDto(source.getSceneDefinition(), index);

        result.setSceneDefinition(singleSceneDefinitionDto);


        return result;
    }

    private static SingleSceneDefinitionDto createFromSceneDefinitionDto(SceneDefinitionDto source, int index) {
        SingleSceneDefinitionDto result = new SingleSceneDefinitionDto();
        result.setSceneNamePrefix(source.getSceneNamePrefix());
        result.setWidth(source.getWidth());
        result.setHeight(source.getHeight());
        result.setNumberOfVerticalLayers(source.getNumberOfVerticalLayers());
        result.setNumberOfObjects(source.getNumberOfObjects());
        result.setSceneNumber(index);
        result.setUseSimulation(source.isUseSimulation());
        result.setGenerationAlgorithm(source.getGenerationAlgorithm());
        result.setAspectRatioId(source.getAspectRatioId());

        return result;
    }
}
