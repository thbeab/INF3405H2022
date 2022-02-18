import java.io.*;
import java.util.Scanner;
import java.net.Socket;

public class Client
{
	private static Socket socket;

	private static boolean verifyIp(String ip){
		String [] addressArray = ip.split("\\.");
		if(addressArray.length != 4){
			return false;
		}

		boolean isCoherent = true;
		for (int i = 0; i < 4 ; i++) {
			int part = Integer.parseInt(addressArray[i]);
			if(part<0 || part>255){
				isCoherent = false;
				break;
			}
		}

		return isCoherent;
	}

	
	public static void main(String[] args) throws Exception {

		// Enter data using BufferReader
		Scanner input = new Scanner(System.in);

		System.out.println("Adresse IP du serveur:");

		String serverAddress = input.nextLine();

		while(!verifyIp(serverAddress)){
			System.out.println("Veuillez entrer une adresse valide:");
			serverAddress = input.nextLine();
		}

		System.out.println("Port d'ecoute:");
		int port = input.nextInt();

		while(port>5050 || port <5000){
			System.out.println("Veuillez rentrer un port entre 5000 et 5050:");
			port = input.nextInt();
		}

		socket = new Socket(serverAddress, port);

		System.out.format("The server is running on %s:%d%n", serverAddress, port);

		PrintWriter out = new PrintWriter(socket.getOutputStream());

		System.out.println("Veuillez entrer votre nom d'utilisateur:");
		String username = input.nextLine();
		out.println(username);

		System.out.println("Veuillez entrer votre mot de passe:");
		String password = input.nextLine();
		out.println(password);
		out.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		socket.close();

		}
	}