package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.controllers.v1;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GeneratePositionsCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.ParseFileCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.TaskCreatedResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services.QueueSender;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services.SocketService;
import org.apache.commons.io.FilenameUtils;
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
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);

            byte[] fileData = file.getBytes();


            ParseFileCommandDto parseFileCommandDto = new ParseFileCommandDto();
            parseFileCommandDto.setFileData(fileData);
            parseFileCommandDto.setFileSuffix("." + extension);

            sender.sendParseFileCommand(id, uuid, parseFileCommandDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
