

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by Mohammad Younesi on 7/28/2018.
 */
public class ChatClient1 {

    static Vector ClientSockets;
    static Vector LoginNames;
//    static String host;
    public static int port = 8821;

    public ChatClient1() throws IOException {
//        this.port = port;
//        this.host = host;
        ServerSocket server = new ServerSocket(port);
        ClientSockets = new Vector();
        LoginNames = new Vector();
        System.out.println("connection port = " + server.getLocalPort());


        while (true){
            Socket client = server.accept();
            AcceptClient acceptClient = new AcceptClient(client);
            System.out.println("new client connected to " + acceptClient.ClientSocket.getRemoteSocketAddress());
        }
    }

    public static void main(String[] args) throws IOException{

        ChatClient1 chatClient1 = new ChatClient1();


    }


}
