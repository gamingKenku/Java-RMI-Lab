import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WordGameSinglePlayerInterface extends Remote {
    void Start() throws RemoteException;
    String SubmitWord(String word) throws RemoteException;
}
