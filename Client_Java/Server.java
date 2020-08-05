import greenfoot.*;
import java.net.*;
import java.io.*;

public class Server extends Actor
{
    int max_clients = 10;

    ServerSocket server;
    Socket[] clients;
    String[] clientNames;
    
    String ip;
    int port = 1357;
    
    GreenfootImage image = new GreenfootImage(1, 1);

    int[] emptySlots = new int[max_clients];

    boolean verbunden = false;

    int anzahl_clients = 0;

    PrintWriter[] pw;
    BufferedReader[] br;

    int[] heartbeat = new int[max_clients];

    public Server() {
        clientNames = new String[max_clients];

        setImage(image);
        
        for(int i = 0; i < max_clients; i++) {
            emptySlots[i] = 0;
            heartbeat[i] = 0;
        }
        
        try
        {
            ip = InetAddress.getLocalHost().toString();
            
            for(int i = 0; i < ip.length(); i++) {
                if(ip.charAt(i) == '/') {
                    ip = ip.substring(i + 1);
                    break;
                }
            }
            
            
            server = new ServerSocket(port);

            clients = new Socket[max_clients];
            pw = new PrintWriter[max_clients];
            br = new BufferedReader[max_clients];

            //System.out.println("Server wurde gestartet!");
            Thread serverListener = new Thread(new ServerListener(server));
            serverListener.start();
        }
        catch (IOException e)
        {
            //System.err.println("Server konnte nicht gestartet werden!");

        }

    }

    public void act() 
    {
        for(int i = 0; i < max_clients; i++) {
            if(emptySlots[i] == 1) {
                heartbeat[i]++;
            }

            if(heartbeat[i] > 500) {
                closeClient(i);
            }
        }
    }   

    public void closeClient(int v) {
        pw[v].println("!!!Du wurdest vom Server gekickt!!!");
        clientNames[v] = "";
        emptySlots[v] = 0;
        try {
            clients[v].close();
        } catch (IOException e) {
        }

    }

    public class ServerListener implements Runnable {
        ServerSocket server;

        public ServerListener(ServerSocket s) {
            try {
                this.server = s;

            } catch (Exception e) {

            }
        }

        public void run() {
            while(true) {
                for(int i = 0; i < max_clients; i++) {
                    if(emptySlots[i] == 0) {
                        try {
                            clients[i] = server.accept();
                            //System.out.println("Client  verbunden!");

                            Thread clientListener = new Thread(new ClientListener(clients[i], i));
                            clientListener.start();

                            emptySlots[i] = 1;
                            anzahl_clients++;
                            i = max_clients;
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }

    public class ClientListener implements Runnable {
        int client_nummer;

        Socket myClient;
        String clientResponse;

        public ClientListener(Socket client, int nummer) {
            try {
                myClient = client;
                client_nummer = nummer;

                pw[client_nummer] = new PrintWriter(myClient.getOutputStream(), true);

                br[client_nummer] = new BufferedReader(new InputStreamReader(myClient.getInputStream()));
                clientNames[client_nummer] = br[client_nummer].readLine();

                for(int i = 0; i < max_clients; i++) {
                    if(emptySlots[i] == 1 && i != nummer) {
                        if(clientNames[i].equals(clientNames[nummer])) {
                            closeClient(nummer);
                        }
                    }
                }
                pw[client_nummer].println("Mit Server verbunden!");
            } catch (IOException e) {

            }

        }

        public void run() {
            while(1 + 1 ==  2) {
                if(Greenfoot.isKeyDown("k")) {
                    for(int i = 0; i < max_clients; i++) {
                        if(emptySlots[i] == 1) {

                            try {
                                clients[i].close();

                            } catch(IOException e) {
                            }
                        }
                    }

                    try {
                        server.close();
                    } catch(IOException e) {
                    }
                }

                try {
                    if((clientResponse = br[client_nummer].readLine()) != null /*&& !last_message.equals(clientResponse)*/) {
                        if(clientResponse.equals("0") || clientResponse.equals("")) {

                        } else if(clientResponse.charAt(0) == '@') {
                            int i = 0;
                            for(; clientResponse.charAt(i) == ' '; i++) {
                                
                            }
                            
                            String block = clientResponse.substring(1, i);
                            
                            for(int v = 0; v < max_clients; v++) {
                                if(emptySlots[v] == 1) {
                                    if(block.equals(clientNames[v])) {
                                        pw[v].println(clientNames[client_nummer] + ": " + clientResponse);
                                    }
                                }
                            }
                        } else {

                            System.out.println(clientNames[client_nummer] + ": " + clientResponse);

                            printMessageToAll(clientResponse, client_nummer);
                        }
                        heartbeat[client_nummer] = 0;
                    }

                } catch(IOException e) {
                }
            }
        }

        public void printMessageToAll(String message, int number) {
            for(int i = 0; i < max_clients; i++) {
                if(emptySlots[i] == 1) {
                    pw[i].println(clientNames[number] + ": " + message);

                }
            }
        }
    }
}

