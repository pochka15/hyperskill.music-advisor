package pochka15.authorization;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Server that can listen to the requests containing authorization code (See Oauth2 authorization code flow for more info)
 */
public class ServerThatWaitsForAuthorizationCode implements AuthCodeSource {

    private final BlockingQueue<String> blockingQueue;

    public ServerThatWaitsForAuthorizationCode() {
        this.blockingQueue = new ArrayBlockingQueue<>(1);
    }

    /**
     * Wait until the server receives an authorization code
     *
     * @return
     */
    @Override
    public String authorizationCode() {
        final Optional<HttpServer> optional = configuredServer();
        if (optional.isPresent()) {
            optional.get().start();
            try {
                return blockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private Optional<HttpServer> configuredServer() {
        try {
            final HttpServer server = HttpServer.create(
                new InetSocketAddress(InetAddress.getLoopbackAddress(), 8080), 0);
            server.createContext("/", exchange -> {
                String code = "";
                final String query = exchange.getRequestURI().getQuery();
                if (query != null) {
                    for (String s : query.split("&")) {
                        if (s.matches("code=.*")) {
                            code = s.substring("code=".length());
                            break;
                        }
                    }
                }
                if (code.isBlank()) {
                    String responseText = "Not found authorization code. Try again.";
                    exchange.sendResponseHeaders(200, responseText.length());
                    exchange.getResponseBody().write(responseText.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    String responseText = "Got the code. Return back to your program.";
                    exchange.sendResponseHeaders(200, responseText.length());
                    exchange.getResponseBody().write(responseText.getBytes());
                    exchange.getResponseBody().close();
                    blockingQueue.add(code);
                    server.stop(0);
                }
            });
            return Optional.of(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
