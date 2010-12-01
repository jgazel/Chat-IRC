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
	
	
	/** Classe permettant de représenter les messages	 */
		private class Message{
			protected int id;
			protected String text = new String("");
			public Message(int id, String text) {this.id = id;	this.text = text;	}
			public int getId() {return id;}		public void setId(int id) {	this.id = id;	}
			public String getText() {return text;}		public void setText(String text) {this.text = text;	}	
		}
	private Vector<Message> allMessages = new Vector<Message>();
		public Vector<Message> getAllMessages() {return allMessages;} 	public void addMessage(int id,String text) {this.allMessages.add(new Message(id, text));}

	/** Classe permettant de représenter les utilisateurs	 */
		private class User{
			protected int id;
			protected String login=new String();
			
			public User(){};
			public User(int id, String login) {this.id = id;	this.login = login;}
	
			public int getId() {return id;}		public void setId(int id) {this.id = id;}
			public String getLogin() {return login;}		public void setLogin(String login) {this.login = login;}

			public String toString() {return "<" + login + ">";	}
			
		}
	protected Vector<User> allUsers = new Vector<User>();
		public Vector<User> getAllUsers() {return allUsers;}	public void addUser(User user) {this.allUsers.add(user);}

		
	public User getUser(int id){
		User user=null;
		for (User u : allUsers){
			if (u.getId()==id) user = u;
		}
		return user;
	}
		
	@Override
	public int getId() throws RemoteException {
		System.out.print("A client ask for connection...");
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
				allUsers.add(new User(id, "DefaultLogin"+id));
				trouve = true;
			}
		}
		System.out.println(" give id::" + id + " done");
		return id;
	}	
		
		
	/**
	 * Reçoit les messages des Clients et les traite
	 * @param x Message envoyé par l'un des clients
	 */
	public void receiveMessage(int id, String x) throws RemoteException
	{
		addMessage(id, x);
		System.out.println(getUser(id).toString() + " " + x);
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