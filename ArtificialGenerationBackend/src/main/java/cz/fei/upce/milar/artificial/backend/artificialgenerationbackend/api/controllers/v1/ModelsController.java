package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.controllers.v1;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.ParseFileCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.TaskCreatedResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.ActiveEndpointsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain.ClientInfo;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services.QueueSender;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services.SocketService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@RequestMapping("${api.base-path}/${models-controller.base-path}/")
@RestController
public class ModelsController {
    private static final Logger logger = LoggerFactory.getLogger(ModelsController.class);

    @Autowired
    private QueueSender sender;

    @Autowired
    private TempFileService tempFileService;

    @Autowired
    private SocketService socketService;

    @Autowired
    private ActiveClientsService activeClientsService;

    @Autowired
    private ActiveEndpointsService activeEndpointsService;

    @PostMapping("${models-controller.start-operation.path}")
    public ResponseEntity<TaskCreatedResponseDto> parseModelFromFile(@RequestHeader("client-id") UUID id,
                                                                     @RequestParam("file") MultipartFile file)
            throws IOException {
        ClientInfo client = activeClientsService.getClient(id);
        UUID uuid = UUID.randomUUID();

        client.addOperationToClient(uuid, 1);
        socketService.sendLogToClient(client.getSocketID(),
                "Request to parse file received, queueing as task with id " + uuid);

        logger.info("Request to parse file received, queueing as task with id {}", uuid);

        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);

        byte[] fileData = file.getBytes();

        ParseFileCommandDto parseFileCommandDto = new ParseFileCommandDto(fileData, "." + extension);

        sender.sendParseFileCommand(id, uuid, parseFileCommandDto);

        TaskCreatedResponseDto responseDto = new TaskCreatedResponseDto(uuid);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("${models-controller.get-value.path}/{operationId}")
    public ResponseEntity<InputStreamResource> getParsedModel(@RequestHeader("client-id") UUID id,
                                                              @PathVariable UUID operationId,
                                                              @RequestParam("index") int operationIndex)
            throws FileNotFoundException {
        logger.info("Fetching result of model parsing for operation with id {}", operationId);

        Path tempFile = tempFileService.getOperationPathAtIndex(id, operationId, operationIndex);

        if (tempFile == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        InputStream stream = new FileInputStream(tempFile.toFile());

        InputStreamResource inputStreamResource = new InputStreamResource(stream);

        return  new ResponseEntity<>(inputStreamResource, HttpStatus.OK);
    }
}
