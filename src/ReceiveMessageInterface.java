import java.rmi.*;
import java.util.Vector;

public interface ReceiveMessageInterface extends Remote
{
	void receiveMessage(int id, String x) throws RemoteException;
	int getId() throws RemoteException;
	void changeLogin(int id, String login) throws RemoteException;
	Vector<String> getLastMessages(int id,int index) throws RemoteException;
	int getLastIndex() throws RemoteException;
	Vector<String> getAllUsersConnected(int id)throws RemoteException;
	int userDisconnection(int id) throws RemoteException;
}

