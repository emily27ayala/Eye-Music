package Server;

import java.io.*;
import java.net.*;

/**
 * classe fille de AbstractServeur qui permet d' avoir un premier serveur
 */
public class FirstServer extends AbstractServer
{
	private String ip = "localhost";
	private ServerSocket ss;

	/**
	 * cette fonction permet d'établir une connection avec le Client
	 * @param ip "address" de base
	 */
	public void connect(String ip) {
		try {
			//the server socket is defined only by a port (its IP is localhost)
			ss = new ServerSocket (6666);
			System.out.println("Server waiting for connection...");
			while (true) {
				Socket socket = ss.accept();//establishes connection 
				System.out.println("Connected as " + ip);	
				// create a new thread to handle client socket	
				new ServerThread(socket).start();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			//if IOException close the server socket
			if (ss != null && !ss.isClosed()) {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

}