package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConvertToJSONTests {

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

        ModelDto modelDto1 = mapper.readValue(modelAsJSON, ModelDto.class);

        System.out.println(modelDto1);
    }
}
