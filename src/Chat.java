import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Chat {
    private String name;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Scanner scanner = new Scanner(System.in);
    private int choice = 0;
    private String ip = null;
    private int port = 0;
    private boolean connected = false;
    private ArrayList<String> messages = new ArrayList<>();

    public void connect(String ip, int port) {
        try{
            connected = false;
            socket = new Socket(ip, port);
            connected = true;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createServer(int port) {
        try{
            connected = false;
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            connected = true;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeBuffer(){
        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try{
            out.writeUTF(message);
            out.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopConnection() {
        try{
            in.close();
            out.close();
            socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receive() {
        try{
            while (true) {
                String message = in.readUTF();
                messages.add(message);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String message){
        message = name + ": " + message;
        messages.add(message);
        System.out.println(message);
        send(message);
    }

    public void update(){
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    receive();
                }finally {
                    stopConnection();
                }
            }
        });
        receiveThread.start();
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return connected;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }
}
