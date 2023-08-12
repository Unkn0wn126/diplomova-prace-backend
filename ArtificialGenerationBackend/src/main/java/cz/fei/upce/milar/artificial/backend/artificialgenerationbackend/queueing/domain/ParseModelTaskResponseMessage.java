package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.ModelDto;

public class ParseModelTaskResponseMessage {
    private String message;
    private ModelDto content;

    @JsonGetter("message")
    public String getMessage() {
        return message;
    }

    @JsonSetter("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonGetter("content")
    public ModelDto getContent() {
        return content;
    }

    @JsonSetter("content")
    public void setContent(ModelDto content) {
        this.content = content;
    }
}
