package homework3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EchoClient extends JFrame {

    private Socket socket;
    private JTextArea chatArea;
    private JTextField inputField;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String login;
    private boolean authorized = false;
    private String nickname;
    private String historyFileName;
    private PrintStream printStream = null;

    public EchoClient(){

        try{
           openConnection();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        initGUI();
    }

    private void openConnection() throws IOException {

        socket = new Socket(ChatConstants.HOST, ChatConstants.PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
            try{

                while(true){

                    String messageFromServer = inputStream.readUTF();
                    String messageToBeAdded = "";
                    if(messageFromServer.startsWith(ChatConstants.AUTH_OK)){
                        chatArea.append(String.format("[%s][Server]: You have been authorized successfully\n", currentTime()));
                    } else if(messageFromServer.equals(ChatConstants.CLOSE_CONNECTION) ||
                            messageFromServer.equals(ChatConstants.STOP_WORD)){
                        shutdownClient();
                    } else if(messageFromServer.startsWith(ChatConstants.CREDENTIALS)){
                        nickname = messageFromServer.split("\\s+")[1];
                        login = messageFromServer.split("\\s+")[2];
                        authorized = true;
                        setTitle(nickname);
                        initializeHistory();
                    } else if(messageFromServer.startsWith(ChatConstants.CLIENTS_LIST)){
                        messageToBeAdded = String.format("[%s][Server]: Now online: %s",
                                currentTime(),
                                Arrays.stream(messageFromServer.split("\\s+"))
                                        .skip(1)
                                        .collect(Collectors.joining(",")));
                        chatArea.append(String.format("%s\n", messageToBeAdded));
                    } else{
                        messageToBeAdded = String.format("[%s]%s", currentTime(), messageFromServer);
                        chatArea.append(String.format("%s\n", messageToBeAdded));
                    }
                    addMessageToHistory(messageToBeAdded);

                }

            }catch(IOException ex){
                ex.printStackTrace();
            }

        }).start();

    }

    private void shutdownClient() {

        chatArea.append(String.format("[%s][Server]: Connection is closing...", currentTime()));
        try {
            Thread.sleep(ChatConstants.SLEEP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeConnection();
        setVisible(false);

    }

    private void closeConnection() {

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
        try{
            printStream.close();
        } catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void initGUI(){

        setBounds(600, 300, 500, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setText(String.format("[%s][Server]: Please type your login and password (/signin *login* *password*)]\n", currentTime()));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setBounds(100, 100, 150, 30);
        panel.add(inputField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        panel.add(sendButton, BorderLayout.EAST);

        add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());

        inputField.addActionListener(e -> sendMessage());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try{
                    outputStream.writeUTF(ChatConstants.STOP_WORD);
                    closeConnection();

                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
        inputField.grabFocus();

    }

    private void sendMessage() {

        if(!inputField.getText().trim().isEmpty()){

            try {

                outputStream.writeUTF(inputField.getText());
                inputField.setText("");
                inputField.grabFocus();

            } catch (IOException e) {

                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while sending message");

            }

        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EchoClient::new);
    }

    private void initializeHistory() throws IOException {
        historyFileName = String.format("%s.txt", login);
        File file = new File(historyFileName);
        if(!file.exists()) {
            file.createNewFile();
        }
        else{
            restoreHistory(file);
        }
        printStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(file,true)));
    }


    private void restoreHistory(File file) {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new ReverseLineInputStream(file)))) {
            List<String> historyList = new ArrayList<>();
            int count = ChatConstants.HISTORY_LINES_QUANTITY;
            while (true && count > 0) {
                String line = null;
                try {
                    line = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (line == null) {
                    break;
                }
                historyList.add(line);
                count--;
            }
            Collections.reverse(historyList);
            chatArea.append(String.join("\n", historyList) + "\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    void addMessageToHistory(String message) {
        if(authorized && !message.trim().isEmpty()){
            printStream.println(message);
        }
    }

    private String currentTime() {
        return new SimpleDateFormat("dd-MM-yy HH:mm:ss").format(System.currentTimeMillis());
    }
}
