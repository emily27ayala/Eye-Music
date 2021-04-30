import Client.SimpleClient;

/**
 * cette class abrite la fonction principale du client
 */
public class ClientConnection
{
	/**
	 * la fonction principale
	 */
	public static void main (String[] args)
	{
		SimpleClient c1 = new SimpleClient();
		c1.connect("localhost");
	}
}