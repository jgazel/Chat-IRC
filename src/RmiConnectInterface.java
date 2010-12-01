import java.rmi.*;

public interface RmiConnectInterface extends Remote{
	void connect(String serverAddress,String serverPort, String login) throws RemoteException;
}
