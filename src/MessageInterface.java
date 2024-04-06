import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageInterface extends Remote {
    public String send(String message) throws RemoteException;
}
