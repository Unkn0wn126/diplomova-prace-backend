package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.ObjectVertexDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.Vector2Dto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.Vector3Dto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

import java.util.ArrayList;

@SpringBootTest
public class ConvertToJSONTests {
    @Test
    public void serializeObjectTest() throws JsonProcessingException {
        ObjectVertexDto vertexDto = new ObjectVertexDto();
        vertexDto.setPosition(new Vector3Dto(0, 0, 0));
        vertexDto.setNormal(new Vector3Dto(0, 0, 0));
        ArrayList<Vector2Dto> texCoords = new ArrayList<>();
        texCoords.add(new Vector2Dto(0, 0));
        vertexDto.setTexCoords(texCoords);
        vertexDto.setTangent(new Vector3Dto(0, 0, 0));

        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setCreateMessageIds(true);

        Message message = messageConverter.toMessage(vertexDto, new MessageProperties());

        ObjectVertexDto objectVertexDto = (ObjectVertexDto) messageConverter.fromMessage(message);

        System.out.println(objectVertexDto);

    }

    @Test
    public void serializeModelTest() throws JsonProcessingException {
        ModelDto modelDto = new ModelDto();
        ModelMeshDto meshDto = new ModelMeshDto();
        modelDto.setMesh(meshDto);
        modelDto.setPosition(new Transform3Dto());

        ModelMeshVertexDto modelMeshVertexDto = new ModelMeshVertexDto();
        modelMeshVertexDto.setPosition(new Transform3Dto());
        modelMeshVertexDto.setNormal(new Transform3Dto());
        modelMeshVertexDto.setTangent(new Transform3Dto());
        modelMeshVertexDto.setTexcoords(new Transform2Dto());

        modelDto.getMesh().getVertices().add(modelMeshVertexDto);

        ObjectMapper mapper = new ObjectMapper();

        String modelAsJSON = mapper.writeValueAsString(modelDto);

        System.out.println(modelAsJSON);
    }
}
