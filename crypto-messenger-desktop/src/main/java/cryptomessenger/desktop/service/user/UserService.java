package cryptomessenger.desktop.service.user;

public interface UserService {

    String getCurrentUsername();

    void register(String username);
}
