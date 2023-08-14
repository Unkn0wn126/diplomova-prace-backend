package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.controllers.v1;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GenerateScenesCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.TaskCreatedResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.ActiveEndpointsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain.ClientInfo;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services.QueueSender;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@RequestMapping("${api.base-path}/${scenes-controller.base-path}/")
@RestController
public class ScenesController {
    private static final Logger logger = LoggerFactory.getLogger(ScenesController.class);

    @Autowired
    private QueueSender sender;

    @Autowired
    private TempFileService tempFileService;

    @Autowired
    private SocketService socketService;

    @Autowired
    private ActiveClientsService activeClientsService;

    @PostMapping("${scenes-controller.start-operation.path}")
    public ResponseEntity<TaskCreatedResponseDto> generatePositions(@RequestHeader("client-id") UUID id,
                                                @RequestBody GenerateScenesCommandDto generatePositionsCommandDto) {
        ClientInfo client = activeClientsService.getClient(id);
        UUID uuid = UUID.randomUUID();

        client.addOperationToClient(uuid, generatePositionsCommandDto.getSceneDefinition().getNumberOfScenes());

        socketService.sendLogToClient(client.getSocketID(),
                "Request to generate scenes received, queueing as operation with id " + uuid);

        logger.info("Request to generate scenes received, queueing as operation with id {}", uuid);

        sender.sendGenerateScenesCommand(id, uuid, generatePositionsCommandDto);

        TaskCreatedResponseDto responseDto = new TaskCreatedResponseDto(uuid);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("${scenes-controller.get-value.path}/{operationId}")
    public ResponseEntity<InputStreamResource> getParsedModel(@RequestHeader("client-id") UUID id,
                                                              @PathVariable UUID operationId,
                                                              @RequestParam("index") int operationIndex)
            throws FileNotFoundException {
        logger.info("Fetching result of scene generation for operation with id {}", operationId);
        // TODO
        Path tempFile = tempFileService.getOperationPathAtIndex(id, operationId, operationIndex);

        if (tempFile == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        InputStream stream = new FileInputStream(tempFile.toFile());

        InputStreamResource inputStreamResource = new InputStreamResource(stream);

        return  new ResponseEntity<>(inputStreamResource, HttpStatus.OK);
    }
}
