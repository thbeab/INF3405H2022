import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.net.Socket;
/**
 * Permet la connexion au clavardage par adresse IP et port
 * @author Thomas Beaugendre (1964902), Marilee Demonceaux (1956712) & Véronica Rabanal-Duchesne (1956734) (gr : 05)
 *
 */
public class Client
{
	private static Socket socket;

	/**
	 * Exécution de l'applicationm Client.
	 * On demande à l'utilisateur de fournir l'ip et le port du serveur
	 * auquel il veut se connecter, puis on lui demande ses identifiants.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		IpUtils.ServerLocation serverLocation = IpUtils.getServerLocation();

		socket = new Socket(serverLocation.ip(), serverLocation.port());

		System.out.format("The server is running on %s:%d%n", serverLocation.ip(), serverLocation.port());

		PrintWriter out = new PrintWriter(socket.getOutputStream());

		Scanner input = new Scanner(System.in);

		System.out.println("Veuillez entrer votre nom d'utilisateur:");
		String username = input.nextLine();
		out.println(username);

		System.out.println("Veuillez entrer votre mot de passe:");
		String password = input.nextLine();
		out.println(password);
		out.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String connectionMessage = in.readLine();

		System.out.println(connectionMessage);

		if(connectionMessage.equals("Erreur dans la saisie du mot de passe")){
			socket.close();
			System.exit(0);
		}
		new WriteThread(socket).start();
		new ReadThread(socket).start();
		}
	}