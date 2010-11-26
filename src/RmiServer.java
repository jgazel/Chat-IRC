import java.rmi.*;
import java.rmi.registry.*;
import java.net.*;

public class RmiServer extends java.rmi.server.UnicastRemoteObject
implements ReceiveMessageInterface
{
	private static final long serialVersionUID = 7290639970974790371L;
	int      thisPort;
	String   thisAddress;
	Registry registry; 
	public void receiveMessage(String x) throws RemoteException
	{
		System.out.println(x);
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