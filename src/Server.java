import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
	private ServerSocket listener;

	private Map<String, String> authenticationMap;

	public Server(){
		authenticationMap = new HashMap<String, String>();
	}

	public void execute() throws IOException {

		Scanner input = new Scanner(System.in);

		System.out.println("Adresse IP du serveur:");
		String serverAddress = input.nextLine();

		while(!verifyIp(serverAddress)){
			System.out.println("Veuillez entrer une adresse valide:");
			serverAddress = input.nextLine();
		}
		System.out.println("Port d'ecoute:");
		int serverPort = input.nextInt();

		while(serverPort>5050 || serverPort <5000){
			System.out.println("Veuillez rentrer un port entre 5000 et 5050:");
			serverPort = input.nextInt();
		}

		this.listener = new ServerSocket();
		this.listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);

		listener.bind(new InetSocketAddress(serverIP, serverPort));

		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);

		try {
			while (true) {
				new ClientHandler(listener.accept(), this).start();
			}
		}
		finally {
			listener.close();
		}

	}

	public boolean userExists(String username){
		return this.authenticationMap.containsKey(username);
	}

	public boolean verifyCredentials(String username, String password){
		return this.authenticationMap.get(username).equals(password);
	}

	private static boolean verifyIp(String ip){
		String [] addressArray = ip.split("\\.");
		if(addressArray.length != 4) {
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
		Server server = new Server();
		server.execute();
	}}
