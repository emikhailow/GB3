package homework2;

import java.util.List;
import java.util.Optional;

public class BaseAuthService implements AuthService{

    public class Entry{

        private String nick;
        private String login;
        private String pass;

        public Entry(String nick, String login, String pass) {
            this.nick = nick;
            this.login = login;
            this.pass = pass;
        }
    }

    private List<Entry> entries;

    public BaseAuthService() {

        entries = List.of(
                new Entry("nick1", "login1", "pass1"),
                new Entry("nick2", "login2", "pass2"),
                new Entry("nick3", "login3", "pass3")
        );

    }

    @Override
    public void start() {
        System.out.println(this.getClass().getName() + " server started");
    }

    @Override
    public void stop() {
        System.out.println(this.getClass().getName() + " server started");
    }

    @Override
    public String getNickname(String login, String pass) {
        return entries.stream()
                .filter(e -> e.login.equals(login) && e.pass.equals(pass))
                .map(e -> e.nick)
                .findFirst().orElse(null);

    }

    @Override
    public boolean updateNickname(String login, String newNickname) {

        Optional<Entry> entry = Optional.of(entries.stream()
                .filter(e -> e.login.equals(login))
                .findFirst().orElse(null));
        if(entry.isEmpty()){
            return false;
        }
        entry.get().nick = newNickname;
        return true;

    }


}
