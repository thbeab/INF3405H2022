import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.*;

/**
 * Server
 * <p>Permet la connexion entre les clients.<p>
 * 
 * @author Thomas Beaugendre (1964902), Marilee Demonceaux (1956712), Véronica Rabanal-Duchesne (1956734)
 *
 */
public class Server {
	private ServerSocket listener;
	private Map<String, String> credentialsMap;
	private Set<ClientHandler> clientHandlers = new HashSet<>();
	private ArrayList<Message> messages;

	public Server() throws IOException {
		this.buildCredentialsMap();
		this.loadMessages();
	}
	
	/**
	 * Rempli la HashMap avec les utilisateurs et passwords de la base de données
	 * @throws IOException
	 */

	private void buildCredentialsMap() throws IOException {
		this.credentialsMap = new HashMap<String, String>();
		BufferedReader br = new BufferedReader(new FileReader("src/users.csv"));
		String line =  null;

		while((line=br.readLine())!=null) {
			String str[] = line.split(",");
			this.credentialsMap.put(str[0].trim().replace("\ufeff", ""), str[1].trim().replace("\ufeff", ""));
		}
	}
	
	/**
	 * Ajoute les messages de la base de données dans une liste
	 * @throws IOException
	 */
	private void loadMessages() throws IOException {
		this.messages = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader("src/messages.csv"));
		String line =  null;
		Message message = null;

		while((line=br.readLine())!=null) {
			String str[] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			if(str.length!=5){
				break;
			}
			message = new Message(str[0], str[1], Integer.parseInt(str[2]), str[3], str[4].trim().replace("\"",""));
			this.messages.add(message);
		}
	}

	/**
	 * Permet de démarrer le serveur
	 * @throws Exception
	 */
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
	
	/**
	 * Permet de vérifier si l'utilisateur existe dans la liste d'utilisateur provenant de la base de données
	 * @param username
	 * @return true si credentialsMap contient l'utilisateur;
	 * 			false sinon.
	 */
	public boolean userExists(String username){
		return this.credentialsMap.containsKey(username);
	}
	
	/**
	 * Permet de vérifier si le mot de passe correspond 
	 * @param username
	 * @param password
	 * @return true si le mot de passe correspond à l'utilisateur;
	 * 			faux, sinon.
	 */

	public boolean verifyCredentials(String username, String password){
		return userExists(username) && this.credentialsMap.get(username).equals(password);
	}

	/**
	 * Permet d'ajouter un utilisateur et le mot de passe s'il n'existe pas dans la liste des utilisateurs
	 * @param username
	 * @param password
	 * @return vrai si un utilisateur a été ajouté à la liste, faux sinon
	 * @throws IOException
	 */
	public boolean addUser(String username, String password) throws IOException {
		if(!userExists(username)){
			this.credentialsMap.put(username, password);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Permet d'ajouter les messages dans la base de données
	 * @param message
	 * @throws IOException
	 */
	public void postMessage(Message message) throws IOException {
		this.messages.add(message);
		FileWriter writer = new FileWriter("src/messages.csv", true);
		String CSVMessage = message.username()+','+message.ip()+','+ message.port()+',' + message.dateTime()+",\""+message.response() +"\"\n";
		writer.append(CSVMessage);
		writer.flush();
		writer.close();
		for (ClientHandler client : clientHandlers) {
			client.sendMessage(message.toString());
		}
	}

	/**
	 * Permet de prendre les 15 derniers messages de la liste des messages provenant de la base de données. 
	 * @return lastMessages
	 */
	public List<Message> get15LastMessages(){
		int messageCount = 0;
		int position = this.messages.size()-1;
		LinkedList<Message> lastMessages = new LinkedList<>();
		while(messageCount < 15 && position >= 0){
			lastMessages.addFirst(messages.get(position));
			position--;
			messageCount++;
		}
		return lastMessages;
	}
	
	/**
	 * Instanciation du serveur
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.execute();
	}
	


	record Message(String username, String ip, int port, String dateTime, String response){
		@Override
		public String toString(){
			return "[" + this.username + " - " + this.ip + ":" + this.port + " - " + this.dateTime + "]: " + this.response;
		}
	}
}
