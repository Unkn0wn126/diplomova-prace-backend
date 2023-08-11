package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ActiveClientsService {
    private static Logger logger = LoggerFactory.getLogger(ActiveClientsService.class);
    private Map<UUID, ClientInfo> activeClients;

    public ActiveClientsService() {
        activeClients = new HashMap<>();
    }

    public void registerClient(UUID clientID) {
        if (activeClients.containsKey(clientID)) {
            logger.warn("Client with this ID was already registered");
        } else {
            activeClients.put(clientID, new ClientInfo(clientID));
        }
    }

    public ClientInfo getClient(UUID clientID) {
        if (!activeClients.containsKey(clientID)) {
            throw new RuntimeException("No such client");
        }

        return activeClients.get(clientID);
    }

    public boolean isClientRegistered(UUID clientID) {
        return activeClients.containsKey(clientID);
    }

    public ClientInfo unregisterClient(UUID clientID) {
        if (!activeClients.containsKey(clientID)) {
            throw new RuntimeException("No such client");
        }

        return activeClients.remove(clientID);
    }

    public void registerClientOperation(UUID clientID, UUID operationID) {
        if (!activeClients.containsKey(clientID)) {
            throw new RuntimeException("No such client");
        }

        activeClients.get(clientID).addOperationToClient(operationID);
    }

    public void removeClientOperation(UUID clientID, UUID operationID) {
        if (!activeClients.containsKey(clientID)) {
            throw new RuntimeException("No such client");
        }

        activeClients.get(clientID).removeOperationFromClient(operationID);
    }

    public void addResultPathToOperation(UUID clientID, UUID operationID, Path path) {
        if (!activeClients.containsKey(clientID)) {
            throw new RuntimeException("No such client");
        }

        activeClients.get(clientID).addResultFilePathToOperation(operationID, path);
    }
}
