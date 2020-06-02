package geekbrains.java_level2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server() {
        Socket socket  = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Server is waiting for connection");
            socket = serverSocket.accept();
            System.out.println("Client is connected");
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            new Thread(new Runnable(){
                @Override
                public void run() {
                    while (true) {
                        String strFromServer = sc.nextLine();
                        try {
                            output.writeUTF(strFromServer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            while(true){
                String strFromClient = input.readUTF();
                System.out.println("Message from Client: " + strFromClient);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                socket.close();
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
