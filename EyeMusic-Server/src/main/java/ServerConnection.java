import Server.AbstractServer;
import Server.FirstServer;

/**
 * cette class abrite la fonction principale du server
 */
public class ServerConnection
{
	/**
	 * la fonction principale
	 */
	public static void main (String[] args) {
		AbstractServer as = new FirstServer();
		String ip = "localhost";
		as.connect(ip);
		
	}
}