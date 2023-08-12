package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.ModelDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.scenes.dto.SceneDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

    public void saveModelToTempFile(UUID clientID, UUID operationID, ModelDto modelDto) throws IOException {
        Path tempFile = Files.createTempFile(TEMP_DIR_NAME, ".tmp");
        tempFile.toFile().deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            ObjectMapper mapper = new ObjectMapper();
            writer.write(mapper.writeValueAsString(modelDto));
        }

        logger.info("Created file " + tempFile.getFileName());

        activeClientsService.addResultPathToOperation(clientID, operationID, tempFile);
    }

    public void saveSceneToTempFile(UUID clientID, UUID operationID, SceneDataDto sceneDataDto) throws IOException {
        Path tempFile = Files.createTempFile(TEMP_DIR_NAME, ".tmp");
        tempFile.toFile().deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            ObjectMapper mapper = new ObjectMapper();
            writer.write(mapper.writeValueAsString(sceneDataDto));
        }

        logger.info("Created file " + tempFile.getFileName());

        activeClientsService.addResultPathToOperation(clientID, operationID, tempFile);
    }

    public List<Path> getTempFiles(UUID clientID, UUID operationID) {
        return activeClientsService.getOperationPaths(clientID, operationID);
    }

    public Path getFirstTempFile(UUID clientID, UUID operationID) {
        return activeClientsService.getOperationPaths(clientID, operationID).stream().findFirst().orElse(null);
    }

    public boolean removeFirstTempFile(UUID clientID, UUID operationID) {
        Path path = activeClientsService.removeFirstOperationPath(clientID, operationID);
        File file = path.toFile();

        return file.delete();
    }
}
