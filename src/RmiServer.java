import java.rmi.*;
import java.rmi.registry.*;
import java.util.Vector;
import java.net.*;

public class RmiServer extends java.rmi.server.UnicastRemoteObject
implements ReceiveMessageInterface
{
	private static final long serialVersionUID = 7290639970974790371L;
	int      thisPort;
	String   thisAddress;
	Registry registry; 
	private Vector<String> allMessages = new Vector<String>();
	// Définition classe privée pour les utilisateurs
	/** Classe permettant de représenter les utilisateurs	 */
		private class User{
			protected int id;
			protected String login;
			
			public User(int id, String login) {this.id = id;	this.login = login;}
	
			public int getId() {return id;}		public void setId(int id) {this.id = id;}
			public String getLogin() {return login;}		public void setLogin(String login) {this.login = login;}
		}
	// Fin définition classe user
	protected Vector<User> allUsers = new Vector<User>();
		public Vector<User> getAllUsers() {return allUsers;}	public void addUser(User user) {this.allUsers.add(user);}
	
		
	@Override
	public int getId() throws RemoteException {
		int id=0;
		Vector<Integer> listeId = new Vector<Integer>();
		for (User user : allUsers){
			listeId.add(user.getId());
		}
		
		boolean trouve=false;
		while(!trouve){
			if (listeId.contains(id)){
				id++;
			}else {
				trouve = true;
			}
		}
		return id;
	}	
		
		
	/**
	 * Reçoit les messages des Clients et les traite
	 * @param x Message envoyé par l'un des clients
	 */
	public void receiveMessage(String x) throws RemoteException
	{
		allMessages.add(x);
		System.out.println(allUsers.size() + " " + x);
	}

	
	
	
	public RmiServer() throws RemoteException
	{
		try{
			thisAddress= (InetAddress.getLocalHost()).toString();
		}
		catch(Exception e){
			throw new RemoteException("can't get inet address.");
		}
		thisPort=3234;  // this port(registry’s port)
		System.out.println("this address="+thisAddress+",port="+thisPort);
		try{
			registry = LocateRegistry.createRegistry( thisPort );
			registry.rebind("rmiServer", this);
		}
		catch(RemoteException e){
			throw e;
		}
	}
	
	/**
	 * Lance un server
	 * @param args Aucun argument n'est nécessaire
	 */
	static public void main(String args[])
	{
		try{
			@SuppressWarnings("unused")
			RmiServer s=new RmiServer();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


}