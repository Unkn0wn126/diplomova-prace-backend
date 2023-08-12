package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto.SceneDataDto;

public class GenerateScenesTaskResponseMessage {
    private String message;
    private SceneDataDto content;

    @JsonGetter("message")
    public String getMessage() {
        return message;
    }

    @JsonSetter("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonGetter("content")
    public SceneDataDto getContent() {
        return content;
    }

    @JsonSetter("content")
    public void setContent(SceneDataDto content) {
        this.content = content;
    }
}
