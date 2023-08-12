package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class AfterConnectResponseDto {
    private String clientID;
    private String apiPort;
    private String parseModelUrl;
    private String generateScenesUrl;

    public AfterConnectResponseDto(String clientID, String apiPort, String parseModelUrl, String generateScenesUrl) {
        this.clientID = clientID;
        this.apiPort = apiPort;
        this.parseModelUrl = parseModelUrl;
        this.generateScenesUrl = generateScenesUrl;
    }

    @JsonGetter("id")
    public String getClientID() {
        return clientID;
    }

    @JsonSetter("apiPort")
    public String getApiPort() {
        return apiPort;
    }

    @JsonSetter("parseModelUrl")
    public String getParseModelUrl() {
        return parseModelUrl;
    }

    @JsonSetter("generateScenesUrl")
    public String getGenerateScenesUrl() {
        return generateScenesUrl;
    }
}
