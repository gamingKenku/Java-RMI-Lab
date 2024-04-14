import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageInterface extends Remote {
    String send(String message) throws RemoteException;
}
