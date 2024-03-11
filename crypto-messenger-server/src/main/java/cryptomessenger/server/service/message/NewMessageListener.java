package cryptomessenger.server.service.message;

public interface NewMessageListener {

    void onNewMessage(Message message);
}
