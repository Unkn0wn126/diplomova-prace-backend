package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer server;

    public SocketIOServerCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) {
        server.start();
    }
}
