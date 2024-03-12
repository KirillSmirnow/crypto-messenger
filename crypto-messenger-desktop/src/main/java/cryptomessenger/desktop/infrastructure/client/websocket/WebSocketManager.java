package cryptomessenger.desktop.infrastructure.client.websocket;

import cryptomessenger.desktop.infrastructure.client.websocket.handler.MessageHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketManager {

    private final Set<MessageHandler> messageHandlers;

    private final WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());
    private StompSession session = null;
    private Set<Subscription> subscriptions = emptySet();

    @Value("${app.server-base-url}")
    private String serverBaseUrl;

    @PostConstruct
    private void initialize() {
        client.setMessageConverter(new StringMessageConverter(StandardCharsets.UTF_8));
    }

    @EventListener(ApplicationReadyEvent.class)
    private void connect() {
        var url = serverBaseUrl.replaceFirst("http", "ws") + "/ws";
        client.connectAsync(url, new SessionHandler(this::connect)).thenAccept(establishedSession -> {
            log.info("Session established");
            session = establishedSession;
            refreshSubscriptions();
        });
    }

    public void refreshSubscriptions() {
        subscriptions.forEach(this::unsubscribe);
        subscriptions = messageHandlers.stream()
                .map(this::subscribe)
                .flatMap(Optional::stream)
                .collect(toSet());
    }

    private void unsubscribe(Subscription subscription) {
        try {
            subscription.unsubscribe();
        } catch (Exception e) {
        }
    }

    private Optional<Subscription> subscribe(MessageHandler handler) {
        var handlerName = handler.getClass().getSimpleName();
        try {
            var destination = handler.getDestination();
            var subscription = session.subscribe(destination, new FrameHandler(handler.getHandler()));
            log.info("Subscribed {} to {}", handlerName, destination);
            return Optional.of(subscription);
        } catch (Exception e) {
            log.warn("Failed to subscribe {}: {}", handlerName, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> getSessionId() {
        return Optional.ofNullable(session == null || !session.isConnected() ? null : session.getSessionId());
    }

    @RequiredArgsConstructor
    private static class SessionHandler extends StompSessionHandlerAdapter {

        private final Runnable onDisconnected;

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.warn("Exception: {}", exception.getMessage());
        }

        @Override
        @SneakyThrows
        public void handleTransportError(StompSession session, Throwable exception) {
            log.warn("Transport error: {}", exception.getMessage());
            Thread.sleep(5000);
            onDisconnected.run();
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
