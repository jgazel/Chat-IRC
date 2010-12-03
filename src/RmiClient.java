import java.io.Console;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class RmiClient implements RmiConnectInterface
{
	static public String serverAddress;
	static public String serverPort;
	static public String text;
	static public Registry registry = null;
	
	/** Id donné par le serveur */
	protected int id;
	/** Login choisi par le client */
	protected String login = new String("Default");
	/** Indice du dernier message reçu */
	protected int index=-1;

	public void connect(String serverAddress, String serverPort, String login)
	throws RemoteException {
		// Se connecte au registre
		// TODO Vérifier que la variable statique n'est pas déjà assigné (interdire deux serveurs)
		RmiClient.serverAddress = serverAddress;
		RmiClient.serverPort = serverPort;
		
		System.out.print("#Connection to "+serverAddress+"::"+serverPort + "...");
		registry=LocateRegistry.getRegistry(
				serverAddress,
				(new Integer(serverPort)).intValue()
		);
		
		
		// Demande une identification
		ReceiveMessageInterface rmiServer;
		try{
			rmiServer=
				(ReceiveMessageInterface)(registry.lookup("rmiServer"));
			id = rmiServer.getId();
			System.out.print("_id::" + id);
			
			// Une fois l'identification reçue, on envoie au serveur le login
			this.login=login;
			rmiServer.changeLogin(id, login);
			System.out.print(" _aka::" +  login );
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}
		
		System.out.println(". done");
	}

	public Vector<String> getLastMessages() throws RemoteException {
		ReceiveMessageInterface rmiServer;
		Vector<String> messages=null;
		System.out.print("#...");
		try{

			rmiServer=
				(ReceiveMessageInterface)(registry.lookup("rmiServer"));
			// Récupère les messages des autres aucune autres infos n'est accessible
			messages = rmiServer.getLastMessages(id,index);
			// Mise à jour de l'index 
			index = rmiServer.getLastIndex();
			for (String message : messages){
				System.out.println(message);
			}
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}

		return messages;
	}
	
	public void send(String text)
	{
		//TODO Condition if afficher connecter vous d'abord si registry=null
		ReceiveMessageInterface rmiServer;
		System.out.print("#Sending "+text+" to "+serverAddress+":"+serverPort+"...");
		try{

			rmiServer=
				(ReceiveMessageInterface)(registry.lookup("rmiServer"));
			rmiServer.receiveMessage(id,text);
			System.out.println(" done");
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}
	}

	public void exit()
	{
		registry = null;
	}
	
	static public void main(String args[])
	{
		try{
			RmiClient c=new RmiClient();

			Console console = System.console();
			if (console != null){
				Boolean exit = false;
				while(!exit){
					String str = console.readLine();
					while (str.equals("")){
						str = console.readLine();
					}	
					

					// Traitement de la ligne rentrée dans la console
					StringTokenizer st = new StringTokenizer(str, " ");
					String keyWord = st.nextToken();

					if (keyWord.equals("connect")){
						String serverA = new String();
						String serverP = new String();
						String login = new String();
						if (st.countTokens() == 3){
							serverA = st.nextToken();
							serverP = st.nextToken();
							login = st.nextToken();
							c.connect(serverA, serverP, login);
						}else{
							System.out.println("Error Connection :" + serverA + "::" + serverP);
						}

					}else if(keyWord.equals("send")){
						String message = st.nextToken();
						while (st.hasMoreTokens()) {
							message = message + " " + st.nextToken();
						}
						c.send(message);
					}else if(keyWord.equals("get")){
						// TODO à décaler dans un thread à part.. ou pas
						c.getLastMessages();
					}else if(keyWord.equals("exit")){
						exit = true;
						c.exit();
					}
					

					
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


}