import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerInterface extends Remote {
    void RequestWord() throws RemoteException;

    String GetName() throws RemoteException;

    void Notify(String message) throws RemoteException;
}
