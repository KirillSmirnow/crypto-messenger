package cryptomessenger.desktop.infrastructure.client.websocket;

import cryptomessenger.desktop.infrastructure.client.websocket.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketManager {

    private final Set<MessageHandler> messageHandlers;

    @Value("${app.server-base-url}")
    private String serverBaseUrl;

    private StompSession session;
    private Set<Subscription> subscriptions = emptySet();

    @EventListener(ApplicationReadyEvent.class)
    private void connect() {
        var client = new WebSocketStompClient(new StandardWebSocketClient());
        client.setMessageConverter(new StringMessageConverter(StandardCharsets.UTF_8));
        client.connectAsync(getConnectionUrl(), new SessionHandler())
                .thenAccept(establishedSession -> {
                    log.info("Connection established");
                    session = establishedSession;
                    refreshSubscriptions();
                });
    }

    private String getConnectionUrl() {
        return serverBaseUrl.replaceFirst("http", "ws") + "/ws";
    }

    public void refreshSubscriptions() {
        subscriptions.forEach(Subscription::unsubscribe);
        subscriptions = messageHandlers.stream().map(handler -> {
            var destination = handler.getDestination();
            var subscription = session.subscribe(destination, new FrameHandler(handler.getHandler()));
            log.info("Subscribed to {}", destination);
            return subscription;
        }).collect(toSet());
    }

    private static class SessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.warn("Transport error: {}", exception.getMessage());
        }
    }

    @RequiredArgsConstructor
    private static class FrameHandler implements StompFrameHandler {

        private final Consumer<String> consumer;

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            consumer.accept((String) payload);
        }
    }
}
