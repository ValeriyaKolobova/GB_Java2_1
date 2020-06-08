package geekbrains.java_level2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String name;

    public String getName(){
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket){
        try{
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        authentication();
                        readMessages();
                    } catch (IOException ex){
                        ex.printStackTrace();
                    } finally {
                        closeConnection();
                    }
                }
            }).start();
        } catch (IOException e){
            throw new RuntimeException("Problems while creating ClientHandler");
        }
    }

    public void authentication() throws IOException {
        while(true){
            String str  = in.readUTF();
            if(str.startsWith("/auth")) {
                String[] parts = str.split("\\s"); 
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1],parts[2]);
                if(nick != null){
                    if(!myServer.isNickBusy(nick)){
                        sendMsg("/authok " + nick);
                        name = nick;
                        myServer.broadcastMsg(name + " logged in");
                        myServer.subscribe(this);
                        return;
                    } else {
                        System.out.println("User under the same nickname already exists.Please choose another nick");
                    }
                } else {
                    System.out.println("Login and password are incorrect OR User can not be found.Please re-enter");
                }
            }
        }
    }

    public void readMessages() throws IOException {
        while(true){
            String strFromClient = in.readUTF();
            if (strFromClient.equals("/end")) {
                return;
            }
            if(strFromClient.startsWith("/w")){
                String[] splitPrivateMsg = strFromClient.split(" ");
                String nickOfReciever = splitPrivateMsg[1];
                String message = splitPrivateMsg[2];
                myServer.sendPrivateMsg(nickOfReciever,message);
            }
            myServer.broadcastMsg(name + ": " + strFromClient); 
        }
    }

    public void sendMsg(String msg){ 
        try{
            out.writeUTF(msg);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void closeConnection(){
        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " left chat");
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
