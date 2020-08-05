import greenfoot.*;
import java.net.*;
import java.io.*;

public class Client extends Actor
{
    Socket server_socket;

    Socket server;
    BufferedReader br;
    PrintWriter pw;
    
    int heartbeat = 0;
    
    String output = new String();
    String lastKey = new String();
    
    GreenfootImage image = new GreenfootImage(1,1);
    
    String ip;
    int port;
    
    public Client(String serverIP, int serverport) {
        this.ip = serverIP;
        this.port = serverport;
        setImage(image);
        try {
            server_socket = new Socket(ip, port);
            Thread listenToServer = new Thread(new ListenToServer(server_socket));
            listenToServer.start();
        } catch(Exception e) {

        }
    }

    public void act() 
    {
        String key = Greenfoot.getKey();
        if(heartbeat < 200) {
            heartbeat++;
        } else if(pw != null) {
            pw.println("0");
            heartbeat = 0;
        }
        
        /*if("enter".equals(key) && !"enter".equals(lastKey)) {
            System.out.println(output);
            pw.println(output);
            output = "";
        } else if(Greenfoot.isKeyDown("space")) {
            output = output.concat(" ");
        } else if(key != null && !"shift".equals(key)) {
            output = output.concat(key);
        }

        lastKey = key;*/
    } 

    public void printToServer(String print) {
        pw.println(print);
    }
    
    public void changeServer(String serverIP, int serverport) {
        this.ip = serverIP;
        this.port = serverport;
        try {
            server_socket = new Socket(ip, port);
        } catch(Exception e) {

        }
    }
    public class ListenToServer implements Runnable {

        String response;
        String last_message;
        public ListenToServer(Socket s) {
            try {
                br = new BufferedReader(new InputStreamReader(server_socket.getInputStream()));
                pw = new PrintWriter(server_socket.getOutputStream(), true);
                pw.println(System.getProperty("user.name"));
            } catch(Exception e) {

            }
        }

        public void run() {
            while(true) {
                try {
                    if((response = br.readLine()) != null /*&& !last_message.equals(response)*/) {
                        //System.out.println("Server response:" + response);
                        getWorld().getObjects(Zwischenserver.class).get(0).print(response);
                    }
                } catch(IOException e) {

                }
            }
        }
    }
}
