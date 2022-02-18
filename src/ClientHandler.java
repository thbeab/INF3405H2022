import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private Socket socket;
    private String username;
    private String password;

    public ClientHandler(Socket socket)
    {
        this.socket = socket;
    }
    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            username = in.readLine();
            password = in.readLine();

            System.out.println(username + " " + password);
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
