import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IpUtils {
    record ServerLocation(String ip, int port) {}

    private static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);

    private IpUtils() {}

    private static boolean isValidInet4Address(String ip)
    {
        if (ip == null) {
            return false;
        }

        Matcher matcher = IPv4_PATTERN.matcher(ip);

        return matcher.matches();
    }

    public static ServerLocation getServerLocation(){
        // Enter data using BufferReader
        Scanner input = new Scanner(System.in);

        System.out.println("Adresse IP du serveur:");

        String ip = input.nextLine();

        while(!isValidInet4Address(ip)){
            System.out.println("Veuillez entrer une adresse valide:");
            ip = input.nextLine();
        }

        System.out.println("Port d'ecoute:");
        int port = input.nextInt();
        input.nextLine();

        while(port>5050 || port <5000){
            System.out.println("Veuillez rentrer un port entre 5000 et 5050:");
            port = input.nextInt();
            input.nextLine();
        }

        return new ServerLocation(ip, port);
    }
}
