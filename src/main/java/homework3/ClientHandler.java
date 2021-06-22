package homework3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHandler {

    private MyServer server;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String name;
    private String login;
    private volatile boolean authorized;
    private volatile long startTime;
    private final Logger LOGGER = LogManager.getLogger();

    public String getName() {
        return name;
    }

    public void sendMessage(String message) {

        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ClientHandler(MyServer server, Socket socket) {

        try {
            this.server = server;
            this.socket = socket;
            this.startTime = System.currentTimeMillis();
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            this.authorized = false;

            new Thread(() -> {
                checkAuthentificationTimeout();
            }).start();

            new Thread(() -> {

                try {
                    autentification();
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }


            }).start();

        } catch (IOException e) {

            LOGGER.error("[Server]: problem while creating client");
            e.printStackTrace();
        }
    }

    private void closeConnection() {

        server.unubscribe(this);
        if(!name.isEmpty()){
            server.broadcastMessage(String.format("[Server]: %s has left chat", name));
            LOGGER.info(String.format("[Server]: user %s has left chat", login));

        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readMessages() throws IOException {

        while(true){

            String messageFromClient = inputStream.readUTF();
            System.out.println("from " + name + ": " + messageFromClient);
            if(authorized) {

                if (messageFromClient.equals(ChatConstants.STOP_WORD)) {
                    return;
                } else if (messageFromClient.startsWith(ChatConstants.SEND_TO_LIST) ||
                        messageFromClient.startsWith(ChatConstants.PERSONAL_MESSAGE)) {

                    String[] splittedString = messageFromClient.split("\\s+");
                    if (splittedString.length < 3) {
                        return;
                    }
                    String keyword = splittedString[0];
                    String nicknamesListString = splittedString[1];
                    String messageText = splittedString[2];
                    List<String> nicknamesList = keyword == ChatConstants.PERSONAL_MESSAGE
                            ? new ArrayList<>() {{
                        add(nicknamesListString.trim());
                    }}
                            : Arrays.asList(nicknamesListString.split("\\s*,\\s*"));

                    server.broadcastMessageToClients(String.format("[%s]: %s", name, messageText), nicknamesList);

                } else if (messageFromClient.startsWith(ChatConstants.CLIENTS_LIST)) {
                    server.broadcastClients();
                } else if (messageFromClient.startsWith(ChatConstants.CHANGE_NICKNAME)) {

                    String newNickname = messageFromClient.split("\\s+")[1].trim();
                    if (server.getAuthService().updateNickname(login, newNickname)) {
                        String exName = name;
                        name = newNickname;
                        server.broadcastMessage(String.format("[Server]: nickname '%s' has been changed to '%s'", exName, newNickname));
                        sendMessage(String.format("%s %s %s", ChatConstants.CREDENTIALS, name, login));
                    } else {
                        server.broadcastMessageToClients(String.format("[Server]: nickname has not been changed due to error"),
                                Arrays.asList(name));
                    }

                } else {
                    server.broadcastMessage(String.format("[%s]: %s", name, messageFromClient));
                }
                LOGGER.info(String.format("[Server] user %s has sent: %s", login, messageFromClient));
            }
        }
    }

    private void autentification() throws IOException {

        String message = inputStream.readUTF();
        if(message.startsWith(ChatConstants.SIGN_IN)){

            String[] parts = message.split("\\s+");
            String nick = server.getAuthService().getNickname(parts[1], parts[2]);
            if(nick != null){

                if(!server.isNickBusy(nick)){

                    authorized = true;
                    sendMessage(ChatConstants.AUTH_OK + " " + nick);
                    name = nick;
                    login = parts[1];
                    server.subscribe(this);

                    sendMessage(String.format("%s %s %s", ChatConstants.CREDENTIALS, name, login));
                    return;

                }else{
                    String info = String.format("[Server]: nickname % has been taken", nick);
                    sendMessage(info);
                    LOGGER.warn(info);
                }

            } else {
                String info = String.format("[Server]: Wrong login or password");
                sendMessage(info);
                LOGGER.warn(info);
            }

        } else if(message.startsWith(ChatConstants.SIGN_UP)){

            String[] parts = message.split("\\s+");
            if(server.getAuthService().createUser(parts[1], parts[2], parts[3])){
                String info = String.format("[Server]: User %s has been successfully created", parts[3]);
                sendMessage(info);
                LOGGER.info(info);
            } else {
                String info = String.format("[Server]: User %s has not been created due to error", parts[3]);
                sendMessage(info);
                LOGGER.error(info);
            }

        }
    }

    private void checkAuthentificationTimeout(){

        while(true){

            if(this.authorized){
                break;
            }else if ((System.currentTimeMillis() - this.startTime) / 1000 > ChatConstants.TIMEOUT){

                String message = String.format("[Server]: You have reached timeout, connection will be closed");
                sendMessage(message);
                LOGGER.info(message);
                sendMessage(ChatConstants.CLOSE_CONNECTION);
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
