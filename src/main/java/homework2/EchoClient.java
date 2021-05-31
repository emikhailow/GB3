package homework2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EchoClient extends JFrame {

    private Socket socket;
    private JTextArea chatArea;
    private JTextField inputField;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

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
                    if(messageFromServer.startsWith(ChatConstants.AUTH_OK)){
                        chatArea.append(String.format("[Server]: You have been authorized successfully\n"));
                    } else if(messageFromServer.equals(ChatConstants.CLOSE_CONNECTION) ||
                            messageFromServer.equals(ChatConstants.STOP_WORD)){
                        shutdownClient();
                    } else if(messageFromServer.startsWith(ChatConstants.TITLE)){
                        setTitle(messageFromServer.split("\\s+")[1]);
                    } else if(messageFromServer.startsWith(ChatConstants.CLIENTS_LIST)){
                        chatArea.append(String.format("[Server]: Now online: %s\n",
                                Arrays.stream(messageFromServer.split("\\s+"))
                                        .skip(1)
                                        .collect(Collectors.joining(","))));
                    } else{
                        chatArea.append(String.format("%s\n", messageFromServer));
                    }

                }

            }catch(IOException ex){
                ex.printStackTrace();
            }

        }).start();

    }

    private void shutdownClient() {

        chatArea.append("[Connection is closing...]");
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

    }

    public void initGUI(){

        setBounds(600, 300, 500, 500);
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setText("[Please type your login and password (/auth yourlogin yourpassword)]\n");
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

}
