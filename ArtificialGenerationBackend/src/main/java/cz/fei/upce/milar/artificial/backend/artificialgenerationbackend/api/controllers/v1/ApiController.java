package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.controllers.v1;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GeneratePositionsCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.ParseFileCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.TaskCreatedResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services.QueueSender;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
public class ApiController {

    @Autowired
    private QueueSender sender;

    @Autowired
    private TempFileService tempFileService;

    @Autowired
    private SocketService socketService;

    @PostMapping("/api/v1/parseModelFromFile/{id}")
    public ResponseEntity<TaskCreatedResponseDto> parseModelFromFile(@PathVariable UUID id, @RequestParam("file")MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        socketService.sendLogToClient(id, "Request to parse file received, queueing as task with id " + uuid);

        try {
            String name = file.getName();
            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();
            byte[] bytes = file.getBytes();

            System.out.println(name);
            System.out.println(contentType);
            System.out.println(originalFilename);
            System.out.println(bytes.length);

            ParseFileCommandDto parseFileCommandDto = new ParseFileCommandDto();
            parseFileCommandDto.setFileData(bytes);
            parseFileCommandDto.setFileSuffix("." + contentType.split("/")[1]);

            sender.sendParseFileCommand(id, uuid, parseFileCommandDto);
        } catch (Exception e) {
            System.out.println(e);
        }

        TaskCreatedResponseDto responseDto = new TaskCreatedResponseDto();
        responseDto.setOperationID(uuid);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/getParsedModel/{id}")
    public ResponseEntity<InputStreamResource> getParsedModel(@PathVariable UUID id, @RequestParam UUID operationId) throws FileNotFoundException {
        List<Path> tempFile = tempFileService.getTempFiles(operationId);

        InputStream stream = new FileInputStream(tempFile.get(0).toFile());

        InputStreamResource inputStreamResource = new InputStreamResource(stream);

        return  new ResponseEntity<>(inputStreamResource, HttpStatus.OK);
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
