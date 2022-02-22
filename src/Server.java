import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
	private ServerSocket listener;
	private Map<String, String> credentialsMap;
	private Set<ClientHandler> clientHandlers = new HashSet<>();
	private ArrayList<Message> messages;

	public Server() throws IOException {
		this.buildCredentialsMap();
		this.messages = new ArrayList<>();

		System.out.println(this.credentialsMap.containsKey("Baba"));
	}

	private void buildCredentialsMap() throws IOException {
		this.credentialsMap = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader("src/users.csv"));
		String line =  null;

		while((line=br.readLine())!=null) {
			String str[] = line.split(",");
			System.out.println(str[0]);
			System.out.println(this.credentialsMap.keySet());
			this.credentialsMap.put(str[0], str[1]);
		}

		this.credentialsMap.put("Koko", "B Ware");
		System.out.println(this.credentialsMap.keySet());
	}

	public void execute() throws Exception {
		IpUtils.ServerLocation serverLocation = IpUtils.getServerLocation();

		this.listener = new ServerSocket();
		this.listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverLocation.ip());

		listener.bind(new InetSocketAddress(serverIP, serverLocation.port()));

		System.out.format("The server is running on %s:%d%n", serverLocation.ip(), serverLocation.port());

		try {
			while (true) {
				ClientHandler client = new ClientHandler(listener.accept(), this);
				this.clientHandlers.add(client);

				client.start();
			}
		}
		finally {
			listener.close();
		}

	}

	public boolean userExists(String username){
		return this.credentialsMap.containsKey(username);
	}

	public boolean verifyCredentials(String username, String password){
		return userExists(username) && this.credentialsMap.get(username).equals(password);
	}

	public boolean addUser(String username, String password){
		if(!userExists(username)){
			this.credentialsMap.put(username, password);
			return true;
		}else{
			return false;
		}
	}

	public Map getMap(){
		return this.credentialsMap;
	}

	public void postMessage(Message message){
		// TODO: implementer la structure du message
		this.messages.add(message);
		String messageString = '[' + message.username() + " - " + message.ip() + ':' + message.port() + "]:" + message.response();
		for (ClientHandler client : clientHandlers) {
			client.sendMessage(messageString);
		}
		System.out.println(messageString);
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.execute();
	}

	record Message(String username, String ip, int port, String response){}
}
