package homework4.chat;

import java.sql.SQLException;

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

   private UsersDatabase usersDatabase;

    public BaseAuthService() {

        usersDatabase = new UsersDatabase();
        try {
            usersDatabase.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public String getNickname(String login, String password) {
        return usersDatabase.getNickname(login, password);
    }

    public boolean updateNickname(String login, String newNickname){
        return usersDatabase.updateNickname(login, newNickname);
    }

    public boolean createUser(String login, String password, String nickname){
        return usersDatabase.createUser(login, password, nickname);
    }

}
