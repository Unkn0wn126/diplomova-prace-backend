package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOServerCommandLineRunner.class);

    /// taken with modifications from: https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
    private final SocketIOServer server;

    public SocketIOServerCommandLineRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
    }
}
