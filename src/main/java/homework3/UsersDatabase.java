package homework3;

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

        try(
                PreparedStatement preparedStatementSelect = connection.prepareStatement("select nickname from users where nickname = ?");
                PreparedStatement preparedStatementUpdate = connection.prepareStatement("update users set nickname = ? where login = ?")
        ){
            preparedStatementSelect.setString(1, newNickname);
            ResultSet resultSet = preparedStatementSelect.executeQuery();
            if(resultSet.next()){
                return false;
            }
            preparedStatementUpdate.setString(1, newNickname);
            preparedStatementUpdate.setString(2, login);
            preparedStatementUpdate.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createUser(String login, String password, String nickname) {

        try(
                PreparedStatement preparedStatementSelect = connection.prepareStatement("select login, nickname from users where login = ? or nickname = ?");
                PreparedStatement preparedStatementInsert = connection.prepareStatement("insert into users (login, password, nickname) values (?, ?, ?)")
        ){
            preparedStatementSelect.setString(1, login);
            preparedStatementSelect.setString(2, nickname);
            ResultSet resultSet = preparedStatementSelect.executeQuery();
            if(resultSet.next()){
                return false;
            }

            preparedStatementInsert.setString(1, login);
            preparedStatementInsert.setString(2, password);
            preparedStatementInsert.setString(3, nickname);
            preparedStatementInsert.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
