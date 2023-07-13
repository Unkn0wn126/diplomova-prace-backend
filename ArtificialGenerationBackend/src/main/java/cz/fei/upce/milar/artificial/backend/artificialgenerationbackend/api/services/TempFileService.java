package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.ModelDto;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class TempFileService {
    private Map<UUID, List<Path>> filesMap;
    private String tempDirName = "tempfiles";

    public TempFileService() {
        try {
            Files.createTempDirectory(tempDirName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        filesMap = new HashMap<>();
    }

    public void saveTempFile(UUID key, byte[] data) throws IOException {
        Path tempFile = Files.createTempFile(tempDirName, ".tmp");
        try (FileOutputStream fs = new FileOutputStream(tempFile.toFile())) {
            fs.write(data);
        }

        System.out.println(tempFile.getFileName());

        if (!filesMap.containsKey(key)) {
            filesMap.put(key, new ArrayList<>());
        }

        filesMap.get(key).add(tempFile);
    }

    public void saveModelToTempFile(UUID key, ModelDto modelDto) throws IOException {
        Path tempFile = Files.createTempFile(tempDirName, ".tmp");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile.toFile()))) {
            ObjectMapper mapper = new ObjectMapper();
            writer.write(mapper.writeValueAsString(modelDto));
        }

        System.out.println(tempFile.getFileName());

        if (!filesMap.containsKey(key)) {
            filesMap.put(key, new ArrayList<>());
        }

        filesMap.get(key).add(tempFile);
    }

    public List<Path> getTempFiles(UUID key) {
        if (!filesMap.containsKey(key)) {
            return new ArrayList<>(); // TODO exception
        }

        return filesMap.get(key);
    }
}
