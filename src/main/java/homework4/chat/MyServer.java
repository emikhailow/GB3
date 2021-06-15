package homework4.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MyServer {

    private AuthService authService;
    private List<ClientHandler> clients;
    private ExecutorService executorService = Executors.newFixedThreadPool(ChatConstants.MAX_CLIENTS_QUAN);

    public MyServer() {

        try(ServerSocket server = new ServerSocket(ChatConstants.PORT)){

            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while(true){

                System.out.println("Server is waiting for connection...");
                Socket socket = server.accept();
                System.out.println("Client connected");
                new ClientHandler(this, socket);

            }


        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(authService != null){
                authService.stop();
            }
        }

    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized boolean isNickBusy(String nick){
        return clients.stream().anyMatch(c -> c.getName().equals(nick));
    }

    public synchronized void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
        broadcastClients();
    }

    public synchronized void unubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
        broadcastClients();
    }

    public synchronized void broadcastMessage(String message){
        clients.forEach(c -> c.sendMessage(message));
    }

    public synchronized void broadcastMessageToClients(String message, List<String> nicknames){

        clients.stream()
                .filter(c -> nicknames.contains(c.getName()))
                .forEach(c -> c.sendMessage(message));

    }

    public synchronized void broadcastClients() {

        String clientsMessage = new StringBuilder(ChatConstants.CLIENTS_LIST)
                .append(" ")
                .append(
                        clients.stream()
                        .map(ClientHandler::getName)
                        .collect(Collectors.joining(" "))
                ).toString();
        clients.forEach(c -> c.sendMessage(clientsMessage));

    }

}
