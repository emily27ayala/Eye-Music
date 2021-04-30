package Server;

/**
 * classe abstraite permettant d'appliquer la substitution de Liskov pour le Serveur
 */
public abstract class AbstractServer
{
	public abstract void connect(String ip);
} 