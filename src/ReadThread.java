import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * ReadThread 
 * 
 * <p>Permet de r�ceptioner les messages.<p>
 * 
 * @author Thomas Beaugendre (1964902), Marilee Demonceaux (1956712), V�ronica Rabanal-Duchesne (1956734)
 *
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;

    /**
     * Cr�er un reader � partir du socket
     * @param socket
     */
    
    public ReadThread(Socket socket) {
        this.socket = socket;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Envoie la r�ponse au serveur
     */
    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println(response);
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
