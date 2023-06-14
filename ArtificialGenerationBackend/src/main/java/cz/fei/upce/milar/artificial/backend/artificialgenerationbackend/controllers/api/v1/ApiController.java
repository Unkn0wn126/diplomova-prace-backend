package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.controllers.api.v1;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.dto.GeneratePositionsCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.dto.ParseFileCommandDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ApiController {

    @PostMapping("/api/v1/parseFile/{id}")
    public ResponseEntity<String> parseFile(@PathVariable UUID id, @RequestBody ParseFileCommandDto parseFileCommandDto) {
        UUID uuid = UUID.randomUUID();
//        socketService.sendMessage(id, "Request to parse file received, queueing as task with id " + uuid);
//        sender.sendParseFileCommand(id, UUID.randomUUID(), parseFileCommandDto);

        return new ResponseEntity<>(uuid.toString(), HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/generatePositions/{id}")
    public ResponseEntity<String> generatePositions(@PathVariable UUID id, @RequestBody GeneratePositionsCommandDto generatePositionsCommandDto) {
        UUID uuid = UUID.randomUUID();
//        socketService.sendMessage(id, "Request to generate positions received, queueing as task with id " + uuid);
//        sender.sendGeneratePositionsCommand(id, UUID.randomUUID(), generatePositionsCommandDto);

        return new ResponseEntity<>(uuid.toString(), HttpStatus.CREATED);
    }
}
