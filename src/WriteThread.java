import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private final Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;

        try {
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error getting output stream: " + e.getMessage());
            e.printStackTrace();
        }
    }

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