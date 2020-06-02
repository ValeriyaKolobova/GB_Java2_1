package geekbrains.java_level2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Client client = new Client();
    }

    public Client () {
       Socket socket = null;
        try {
            socket = new Socket("localhost", 8189);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String strFromServer = null;
                        try {
                            strFromServer = input.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Message from Server: " + strFromServer);
                    }
                }
            }).start();

            while (true) {
                String strFromClient = sc.nextLine();
                output.writeUTF(strFromClient);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}


