package homework2;

public interface AuthService {

    void start();
    void stop();

    String getNickname(String login, String password);
    default boolean updateNickname(String login, String newNickname){
        throw new IllegalStateException("Can not be used in this implementation");
    }
    default boolean createUser(String login, String password, String nickname){
        throw new IllegalStateException("Can not be used in this implementation");
    }

}
