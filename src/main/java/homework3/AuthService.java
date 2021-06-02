package homework3;

public interface AuthService {

    void start();
    void stop();

    String getNickname(String login, String password);
    boolean updateNickname(String login, String newNickname);
    boolean createUser(String login, String password, String nickname);

}
