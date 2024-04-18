import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WordsGameMultiPlayerInterface extends Remote {
    enum ValidationResponse{
        PASSED,
        NO_INPUT,
        WORD_ALREADY_CALLED,
        WORD_NOT_FOUND,
        WRONG_FIRST_LETTER,
        TIME_OUT,
        FORCE_EXIT,
    }
    void Start() throws RemoteException;
    WordsGameMultiPlayerInterface.ValidationResponse SubmitWord(String word) throws RemoteException;
    String GetLastWord() throws RemoteException;
    void RegisterPlayer(PlayerInterface player_stub) throws RemoteException;
}
