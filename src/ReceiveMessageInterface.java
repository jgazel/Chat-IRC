import java.rmi.*;

public interface ReceiveMessageInterface extends Remote
{
	void receiveMessage(int id, String x) throws RemoteException;
	int getId() throws RemoteException;
}

