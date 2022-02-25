import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Vérifie la validité de l'adresse IP et du port entrés dans le serveur et le client
 * @author Thomas Beaugendre (1964902), Marilee Demonceaux (1956712) & Véronica Rabanal-Duchesne (1956734) (gr : 05)
 *
 */
public final class IpUtils {
    record ServerLocation(String ip, int port) {}

    private static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);
    
    private static final String PORT_REGEX = "^(50[0-4][0-9]|5050)$";

    private static final Pattern PORT_PATTERN = Pattern.compile(PORT_REGEX);

    private IpUtils() {}
    
    /**
     * Vérification du format d'une chaine de caractères selon un pattern donné
     * @param string la chaine à vérifier
     * @param pattern le format désiré
     * @return si la chaine correspond au pattern
     */
    private static boolean isValidPattern(String string, Pattern pattern){
        if (string == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(string);

        return matcher.matches();
    }
    
    /**
     * Analyse l'entrée des informations sur le serveur.
     * S'assure que les informations fournies sont conformes
     * @return l'objet ServerLocation contenant l'ip et le port du serveur
     */
    public static ServerLocation getServerLocation(){
        Scanner input = new Scanner(System.in);

        System.out.println("Adresse IP du serveur:");

        String ip = input.nextLine();

        while(!isValidPattern(ip, IPv4_PATTERN)){
            System.out.println("Veuillez entrer une adresse valide:");
            ip = input.nextLine();
        }

        System.out.println("Port d'ecoute:");
        String port = input.nextLine();

        while(!isValidPattern(port, PORT_PATTERN)){
        	System.out.println("Veuillez rentrer un port entre 5000 et 5050:");
            port = input.nextLine();
        }

        return new ServerLocation(ip, Integer.parseInt(port));
    }
}
