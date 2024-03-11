package cryptomessenger.desktop.infrastructure.client.websocket.handler;

import cryptomessenger.desktop.infrastructure.client.user.UserClient;
import cryptomessenger.desktop.infrastructure.localstorage.LocalStorage;
import cryptomessenger.desktop.service.LocalStorageKeys;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class NewMessageHandler implements MessageHandler {

    private final LocalStorage localStorage;
    private final UserClient userClient;

    @Setter
    private Runnable onNewMessage;

    @Override
    public String getDestination() {
        var username = localStorage.getString(LocalStorageKeys.CURRENT_USERNAME);
        var user = userClient.getByUsername(username);
        return "/user/%s/messages/new".formatted(user.getId());
    }

    @Override
    public Consumer<String> getHandler() {
        return message -> {
            if (onNewMessage != null) {
                onNewMessage.run();
            }
        };
    }
}
