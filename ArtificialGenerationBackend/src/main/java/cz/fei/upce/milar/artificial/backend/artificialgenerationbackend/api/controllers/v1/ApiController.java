package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.controllers.v1;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GeneratePositionsCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.ParseFileCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.TaskCreatedResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.*;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services.QueueSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ApiController {

    @Autowired
    private QueueSender sender;

    @PostMapping("/api/v1/parseFile/{id}")
    public ResponseEntity<TaskCreatedResponseDto> parseFile(@PathVariable UUID id, @RequestBody ParseFileCommandDto parseFileCommandDto) {
        UUID uuid = UUID.randomUUID();
//        socketService.sendMessage(id, "Request to parse file received, queueing as task with id " + uuid);
        //sender.sendParseFileCommand(id, uuid, parseFileCommandDto);

        TaskCreatedResponseDto responseDto = new TaskCreatedResponseDto();
        responseDto.setOperationID(uuid);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/getParsedModel")
    public ResponseEntity<ModelDto> getParsedModel() {
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

        ModelMeshFaceDto meshFaceDto = new ModelMeshFaceDto();
        meshFaceDto.getIndices().add(0);

        modelDto.getMesh().getFaces().add(meshFaceDto);;

        return  new ResponseEntity<>(modelDto, HttpStatus.OK);
    }

    @PostMapping("/api/v1/generatePositions/{id}")
    public ResponseEntity<TaskCreatedResponseDto> generatePositions(@PathVariable UUID id, @RequestBody GeneratePositionsCommandDto generatePositionsCommandDto) {
        UUID uuid = UUID.randomUUID();
//        socketService.sendMessage(id, "Request to generate positions received, queueing as task with id " + uuid);
        sender.sendGeneratePositionsCommand(id, uuid, generatePositionsCommandDto);

        TaskCreatedResponseDto responseDto = new TaskCreatedResponseDto();
        responseDto.setOperationID(uuid);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
