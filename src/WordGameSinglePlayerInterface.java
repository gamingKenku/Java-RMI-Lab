import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WordGameSinglePlayerInterface extends Remote {
    boolean SubmitWord(String word) throws RemoteException;
}
