import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * WriteThread
 * <p>Permet de saisir une r�ponse dans le client.<p>
 * @author Thomas Beaugendre (1964902), Marilee Demonceaux (1956712), V�ronica Rabanal-Duchesne (1956734)
 *
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private final Socket socket;
    
    /**
     * Cr�er un writer � partir du socket
     * @param socket
     */

    public WriteThread(Socket socket) {
        this.socket = socket;

        try {
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error getting output stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Affiche la r�ponse dans le client
     */
    public void run() {
        String response;
        Scanner input = new Scanner(System.in);
        do {
            response = input.nextLine();
            if(response.length()<200) {
                writer.println(response);
                writer.flush();
            }
            else{
                System.out.println("Votre message ne peut pas dépasser 200 caractères");
            }
        } while (!response.equals("disconnect"));

        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}