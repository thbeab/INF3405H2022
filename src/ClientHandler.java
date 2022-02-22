import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

public class ClientHandler extends Thread
{
    private final Socket socket;
    private final String ip;
    private final int port;
    private final Server chatServer;
    private String username;
    private PrintWriter out;

    public ClientHandler(Socket socket, Server server)
    {
        this.socket = socket;
        this.chatServer = server;
        this.port = socket.getPort();
        this.ip = socket.getInetAddress().getHostAddress();
        try {
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            System.out.println("Error handling client " + e);
        }
    }

    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = in.readLine();
            String password = in.readLine();

            System.out.println(this.username + " bobette " + password);

            boolean isConnected = false;

            if(chatServer.userExists(username)){
                System.out.println("User exists");
                if(!chatServer.verifyCredentials(username, password)){
                    out.println("Erreur dans la saisie du mot de passe");
                    out.flush();
                }else
                    isConnected=true;
            }else{
                chatServer.addUser(username, password);
                isConnected = true;
            }
            System.out.println(chatServer.getMap());
            out.println("Conecté a la salle de chat");
            out.flush();

            String message;

            while(isConnected){
                message = in.readLine();
                if(message!=null && message.equals("disconnect")){
                    isConnected = false;
                }
                chatServer.postMessage(new Server.Message(this.username, this.ip, this.port, message));
            }

        } catch (IOException e)
        {
            System.out.println("Error handling client " + e);
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(IOException e){
                System.out.println("Couldn't close  a socket, what's going on?");
            }
            System.out.println("Connection with client# " + " closed");
        }
    }
}
