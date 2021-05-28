package homework2;

import java.sql.*;

public class UsersDatabase {

    static final String DATABASE_URL = "jdbc:sqlite:homework2.db";
    static Connection connection;
    static Statement statement;

    static {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void createTable() throws SQLException {

        if(!tableExists("users")){
            String createTable = "create table users (" +
                    "id integer not null primary key, " +
                    "nickname varchar(30) not null," +
                    "login varchar(30) not null unique," +
                    "password varchar(30) not null)";
            statement.execute(createTable);
        }

    }

    public boolean tableExists(String table){

        DatabaseMetaData databaseMetaData = null;
        try {
            databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, table, null);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void insertUser(String login, String password, String nickname){

        try(PreparedStatement preparedStatement = connection.prepareStatement("select login from users where login = ?")){
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into users (login, password, nickname) values (?, ?, ?)")){
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nickname);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String getNickname(String login, String password) {

        try(PreparedStatement preparedStatement = connection.prepareStatement("select nickname from users where login = ? and password = ?")){
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean updateNickname(String login, String newNickname) {

        try(PreparedStatement preparedStatement = connection.prepareStatement("select nickname from users where nickname = ?")){
            preparedStatement.setString(1, newNickname);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("update users set nickname = ? where login = ?")){
            preparedStatement.setString(1, newNickname);
            preparedStatement.setString(2, login);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean createUser(String login, String password, String nickname) {

        try(PreparedStatement preparedStatement = connection.prepareStatement("select login, nickname from users where login = ? or nickname = ?")){
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, nickname);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into users (login, password, nickname) values (?, ?, ?)")){
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nickname);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }
}
