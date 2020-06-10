package geekbrains.java_level2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public boolean authorized;

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public Client(){
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(120000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if (!authorized){
                        try{
                            socket.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            setAuthorized(false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String strFromServer = in.readUTF();
                            if (strFromServer.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            }
                            System.out.println(strFromServer + "\n");
                        }
                        while (true) {
                            String strFromServer = in.readUTF();
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                break;
                            }
                            System.out.println(strFromServer);
                            System.out.println("\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
