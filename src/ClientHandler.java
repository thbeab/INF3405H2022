import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class ClientHandler extends Thread
{
    private Socket socket;
    private Server server;

    public ClientHandler(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
    }

    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String username = in.readLine();
            String password = in.readLine();

            System.out.println(username + " " + password);

            if(server.userExists(username)){
                if(server.verifyCredentials(username, password)){
                    
                }
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
