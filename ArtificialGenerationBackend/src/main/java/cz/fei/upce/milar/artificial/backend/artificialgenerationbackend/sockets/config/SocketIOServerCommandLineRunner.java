package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerCommandLineRunner implements CommandLineRunner {

    /// taken with modifications from: https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
    private final SocketIOServer server;

    public SocketIOServerCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting...");
        server.start();
    }
    ///
}
