package cryptomessenger.desktop.infrastructure.client.websocket.handler;

import java.util.function.Consumer;

public interface MessageHandler {

    String getDestination();

    Consumer<String> getHandler();
}
