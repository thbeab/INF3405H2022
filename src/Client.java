import java.io.BufferedReader;
import java.io.DataInputStream;
import java.util.Scanner;
import java.net.Socket;

public class Client
{
	private static Socket socket;
	
	public static void main(String[] args) throws Exception {

		// Enter data using BufferReader
		Scanner input = new Scanner(System.in);

		System.out.println("Adresse IP du serveur:");

		String serverAddress = input.nextLine();

		String [] addressArray = serverAddress.split("\\.");

		int[] ip = new int[4];

		boolean isCoherent = true;
		for (int i = 0; i < 4 || isCoherent; i++) {
			ip[i] = Integer.parseInt(addressArray[i]);
			if(ip[i]<0 || ip[i]>255){
				isCoherent = false;
			}
		}



		System.out.println("Port d'ecoute:");
		int port = input.nextInt();

		while(port>5050 || port <5000){
			System.out.println("Veuillez rentrer un port entre 5000 et 5050:");
			port = input.nextInt();
		}
		
		socket = new Socket(serverAddress, port);
		
		System.out.format("The server is running on %s:%d%n", serverAddress, port);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		socket.close();

		}
	}