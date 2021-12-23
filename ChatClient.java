
import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


/**
 * Created by Mohammad Younesi on 7/28/2018.
 */
public class ChatClient extends JFrame implements Runnable {

    Socket socket;
    JTextArea ta;
    JButton send , logout ;
    JTextField tf;

    Thread thread;

    DataInputStream din;
    DataOutputStream dout;

    String LoginName;
    String hostname;
    int portNum;

    public ChatClient(String login, String host, int portNum) throws IOException{
        super(login);
        LoginName=login;
        this.hostname = host;
        this.portNum = portNum;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dout.writeUTF(LoginName + " " + "LOGOUT");
                    System.out.println(LoginName + " " + "LOGOUT");
                    System.exit(1);
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        });

        ta = new JTextArea(18,50);
        tf = new JTextField(50);

        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER ) {
                    try {
                        if(tf.getText().length()>0) {
                            dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
                            System.out.println(LoginName + " " + "DATA " + tf.getText().toString());
                        }
                        tf.setText("");

                    } catch (IOException e1){
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        send = new JButton("send");
        logout = new JButton("logout");

        logout.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dout.writeUTF(LoginName + " " + "LOGOUT" );
                    System.out.println(LoginName + " " + "LOGOUT");
                    System.exit(1);
                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        });

        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean flag = false ;
                    if(tf.getText().length()>0) {

                        dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
                        System.out.println(LoginName + " " + "DATA " + tf.getText().toString());

                        if(tf.getText().toString().contains("bye")){
                            flag = true;
                            System.out.println("someOne has logged out");
                        }
                    }
                    System.out.println();
                    tf.setText("");

                    if(flag){

                        logout.doClick();
                        dout.writeUTF(LoginName + " " + "LOGOUT" );
                        System.out.println("someOne has logged out");
                        System.exit(1);

                    }

                } catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        });



        socket = new Socket(this.hostname,this.portNum);
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());

        dout.writeUTF(LoginName);
        System.out.println(LoginName);
        dout.writeUTF(LoginName+" "+"LOGIN");
        System.out.println(LoginName+" "+"LOGIN");

        thread = new Thread(this);
        thread.start();
        setup();
    }

    public void setup(){
        setSize(600,500);

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(ta));
        panel.add(tf);
        panel.add(send);
        panel.add(logout);

        add(panel);

        setVisible(true);


    }
    @Override
    public void run() {
        while (true){
            try{
                ta.append("\n"+ din.readUTF());
                //System.out.println("************");
                //System.out.println(din.readUTF());
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        String host, username;
//        String[] arguments = new String[] {"123"};
//        ChatServer.main(arguments);
        int port;
        final int MAXPEERS = 5;
        ChatClient def = new ChatClient("mohammad","localhost",ChatClient1.port);
        Scanner scanner = new Scanner(System.in);
        for (int i=1; i < MAXPEERS; i++){
            System.out.println("enter peer " + i + " ip address : ");
            host = scanner.next();
            System.out.println("enter peer " + i + " port number : ");
            port = scanner.nextInt();
            System.out.println("enter peer " + i + " username : ");
            username = scanner.next();

            if(port == ChatClient1.port){
                ChatClient chatClient = new ChatClient(username,host,port);
            }else{
                System.out.println("peer not found");
            }
        }
    }
}
