import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

public class ClientHandler extends Thread
{
    private final Socket socket;
    private final String ip;
    private final int port;
    private final Server chatServer;
    private String username;
    private PrintWriter out;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");

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
                FileWriter writer = new FileWriter("src/users.csv", true);
                writer.append(username+','+password+'\n');
                writer.flush();
                writer.close();
                isConnected = true;
            }

            if(isConnected) {
                out.println("Connecté a la salle de chat");
                out.flush();
                List<Server.Message> messageList = chatServer.get15LastMessages();
                for(Server.Message m:messageList){
                    out.println(m.toString());
                }
                out.flush();
            }

            String message;
            LocalDateTime dateTime;
            while(isConnected){
                message = in.readLine();
                dateTime = LocalDateTime.now();
                chatServer.postMessage(new Server.Message(this.username, this.ip, this.port, dateFormat.format(dateTime), message));
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
