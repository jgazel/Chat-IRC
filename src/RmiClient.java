import java.io.Console;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.Scanner;
import java.util.StringTokenizer;

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


	public void connect(String serverAddress, String serverPort)
	throws RemoteException {
		// Se connecte au registre
		RmiClient.serverAddress = serverAddress;
		RmiClient.serverPort = serverPort;
		System.out.print("Connection to "+serverAddress+"::"+serverPort+ "...");
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
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}
		
		System.out.println(" done");
	}

	
	public void send(String text)
	{
		//TODO Condition if afficher connecter vous d'abord si registry=null
		ReceiveMessageInterface rmiServer;
		System.out.println("sending "+text+" to "+serverAddress+":"+serverPort);
		try{

			rmiServer=
				(ReceiveMessageInterface)(registry.lookup("rmiServer"));
			rmiServer.receiveMessage(id,text);
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}
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
						if (st.countTokens() == 2){
							serverA = st.nextToken();
							serverP = st.nextToken();
							c.connect(serverA, serverP);
						}else{
							System.out.println("Error Connection :" + serverA + "::" + serverP);
						}

					}else if(keyWord.equals("send")){
						String message = st.nextToken();
						while (st.hasMoreTokens()) {
							message = message + " " + st.nextToken();
						}
						c.send(message);
					}else if(keyWord.equals("exit")){
						exit = true;
						registry = null;
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