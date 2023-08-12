package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.GenerateScenesTaskResponseMessage;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.ParseModelTaskResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TempFileService {
    private static final Logger logger = LoggerFactory.getLogger(TempFileService.class);

    private final String TEMP_DIR_NAME = "tempfiles";
    private final ActiveClientsService activeClientsService;
    private Map<UUID, List<Path>> filesMap;

    public TempFileService(ActiveClientsService activeClientsService) {
        this.activeClientsService = activeClientsService;

        try {
            Files.createTempDirectory(TEMP_DIR_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        filesMap = new HashMap<>();
    }

    public void saveModelToTempFile(UUID clientID, UUID operationID, ParseModelTaskResponseMessage responseMessage)
            throws IOException {
        Path tempFile = Files.createTempFile(TEMP_DIR_NAME, ".tmp");
        tempFile.toFile().deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            ObjectMapper mapper = new ObjectMapper();
            writer.write(mapper.writeValueAsString(responseMessage.getContent()));
        }

        logger.info("Created file " + tempFile.getFileName());

        activeClientsService.addResultPathToOperation(clientID, operationID, tempFile,
                responseMessage.getOperationIndex());
    }

    public void saveSceneToTempFile(UUID clientID, UUID operationID, GenerateScenesTaskResponseMessage responseMessage)
            throws IOException {
        Path tempFile = Files.createTempFile(TEMP_DIR_NAME, ".tmp");
        tempFile.toFile().deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            ObjectMapper mapper = new ObjectMapper();
            writer.write(mapper.writeValueAsString(responseMessage.getContent()));
        }

        logger.info("Created file " + tempFile.getFileName());

        activeClientsService.addResultPathToOperation(clientID, operationID, tempFile,
                responseMessage.getOperationIndex());
    }

    public Path[] getTempFiles(UUID clientID, UUID operationID) {
        return activeClientsService.getOperationPaths(clientID, operationID);
    }

    public Path getOperationPathAtIndex(UUID clientID, UUID operationID, int index) {
        return activeClientsService.getOperationPathAtIndex(clientID, operationID, index);
    }
}
