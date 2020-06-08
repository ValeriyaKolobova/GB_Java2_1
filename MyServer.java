package geekbrains.java_level2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() { 
        return authService;
    }

    public MyServer(){
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true){
                System.out.println("Server awaits connection with client");
                Socket socket  = server.accept();
                System.out.println("Client connected");
                new ClientHandler(this, socket);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick){
        for (ClientHandler o: clients){
            if(o.getName().equals(nick)) return true;
        }
        return false;
    }

    public synchronized void broadcastMsg (String msg){
        for (ClientHandler o: clients){
            o.sendMsg(msg);
        }
    }

    public synchronized void sendPrivateMsg(String nickOfReceiver, String message){
        for (ClientHandler o: clients){
            if(o.getName().equals(nickOfReceiver)){
                o.sendMsg(message);
            }
        }
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o); 
    }

    public synchronized void subscribe(ClientHandler o){
        clients.add(o);
    }
}
