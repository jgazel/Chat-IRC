import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;

/** Thread whiwh will take the messages */
public class GetMessage extends Thread{
	protected RmiClient _client;

	protected static int THREAD_DELAY = 600;

	public GetMessage(RmiClient client)
    {
      super("GetMessageThread");
      _client = client;
    }

    /**
	 * Récupère les messages non lus sur le serveur
	 * @return un vecteur des chaines de caractères des messages
	 * @throws RemoteException
	 */
	public void getLastMessages() throws RemoteException {
		ReceiveMessageInterface rmiServer;
		Vector<String> messages=null;
		try{

			rmiServer=
				(ReceiveMessageInterface)(RmiClient.registry.lookup("rmiServer"));
			if (RmiClient.registry !=null){
				// Récupère les messages des autres aucune autres infos n'est accessible
				messages = rmiServer.getLastMessages(_client.getId(),_client.getIndex());
				// Mise à jour de l'index 
				_client.setIndex(rmiServer.getLastIndex());
				for (String message : messages){
					System.out.println(message);
				}
			}
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}
	}
	

    public void run()
    {
      System.out.println("...Initialisation");
      while(true){
    	  try {
			Thread.sleep(THREAD_DELAY);
			try {
				getLastMessages();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
      }
    }
    

    public RmiClient get_client() {
		return _client;
	}

}
