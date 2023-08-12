package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActiveEndpointsService {

    @Value("${api.base-path}")
    private String apiBasePath;

    @Value("${server.port}")
    private String apiPort;
    @Value("${models-controller.base-path}")
    private String modelsControllerBasePath;
    @Value("${models-controller.start-operation.path}")
    private String parseModelUrl;

    @Value("${models-controller.get-value.path}")
    private String modelResultUrl;
    @Value("${scenes-controller.base-path}")
    private String scenesControllerBasePath;
    @Value("${scenes-controller.start-operation.path}")
    private String generateScenesUrl;
    @Value("${scenes-controller.get-value.path}")
    private String sceneResultUrl;

    public String getApiPort() {
        return apiPort;
    }

    public String composeParseModelPath() {
        return composeModelsBaseUrl() + parseModelUrl;
    }

    public String composeModelResultPath() {
        return composeModelsBaseUrl() + modelResultUrl;
    }

    public String composeGenerateScenesPath() {
        return composeScenesBaseUrl() + generateScenesUrl;
    }

    public String composeSceneResultPath() {
        return composeScenesBaseUrl() + sceneResultUrl;
    }

    private String composeModelsBaseUrl() {
        return apiBasePath + "/" + modelsControllerBasePath + "/";
    }

    private String composeScenesBaseUrl() {
        return apiBasePath + "/" + scenesControllerBasePath + "/";
    }
}
