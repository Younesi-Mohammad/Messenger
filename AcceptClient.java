

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;



/**
 * Created by MohammadYounesi on 7/28/2018.
 */


public class AcceptClient extends Thread {

    Socket ClientSocket;
    DataInputStream din;
    DataOutputStream dout;

    public AcceptClient(Socket clientSocket) throws IOException {
        ClientSocket = clientSocket;
        din = new DataInputStream(ClientSocket.getInputStream());
        dout = new DataOutputStream(ClientSocket.getOutputStream());

        String LoginName = din.readUTF();
        int flag = 0;
        if(ChatClient1.LoginNames.size()>0){
            for (int i=0 ; i<ChatClient1.LoginNames.size();i++){
                if(LoginName.equals(ChatClient1.LoginNames.elementAt(i))){
                    flag++;
                }
            }
        }

        if(flag==0) {
            ChatClient1.LoginNames.add(LoginName);
            ChatClient1.ClientSockets.add(ClientSocket);
        }else {
            System.out.println("This Username is already taken , please try another Username!");
        }

        start();
    }

    public void run(){
        while (true){
            try{
                String msgFromClient = din.readUTF();
                StringTokenizer st = new StringTokenizer(msgFromClient);
                String LoginName = st.nextToken();
                String msgType = st.nextToken();
                String msg = "";
                int Lo = -1 ;
                while (st.hasMoreTokens()){
                    msg = msg + " "+st.nextToken();
                }

                if(msgType.equals("LOGIN") ){
                    for (int i=0 ; i<ChatClient1.LoginNames.size();i++){
                        Socket pSocket = (Socket) ChatClient1.ClientSockets.elementAt(i);
                        DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                        pOut.writeUTF(LoginName + " has logged in");
                        System.out.println(LoginName + " has logged in");
                    }
                }else if(msgType.equals("LOGOUT")){
                    for (int i=0 ; i<ChatClient1.LoginNames.size();i++){
                        if(LoginName.equals(ChatClient1.LoginNames.elementAt(i))){
                            Lo = i ;
                        }
                        Socket pSocket = (Socket) ChatClient1.ClientSockets.elementAt(i);
                        DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                        pOut.writeUTF(LoginName + " has logged out");
                        System.out.println(LoginName + " has logged out");
                    }
                    if(Lo >=0){
                        ChatClient1.LoginNames.removeElementAt(Lo);
                        ChatClient1.ClientSockets.removeElementAt(Lo);
                    }
                }else if(msgType.equals("DATA")) {
                    for (int i=0 ; i<ChatClient1.LoginNames.size();i++){
                        Socket pSocket = (Socket) ChatClient1.ClientSockets.elementAt(i);
                        DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                        pOut.writeUTF(LoginName + " : "+msg);
                        System.out.println(LoginName + " : "+msg);
                    }
                }

                if(msg.contains("bye")){
                    System.out.println("some one said bye :D ");
                    for (int i=0 ; i<ChatClient1.LoginNames.size();i++){
                        if(LoginName.equals(ChatClient1.LoginNames.elementAt(i))){
                            Lo = i ;
                        }
                        Socket pSocket = (Socket) ChatClient1.ClientSockets.elementAt(i);
                        DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                        pOut.writeUTF(LoginName + " has logged out");
                    }
                    if(Lo >=0){
                        ChatClient1.LoginNames.removeElementAt(Lo);
                        ChatClient1.ClientSockets.removeElementAt(Lo);
                    }

                }

                if(msg.contains("bye")){
                    break;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
