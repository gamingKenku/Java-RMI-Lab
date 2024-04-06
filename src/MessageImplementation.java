import java.rmi.RemoteException;

public class MessageImplementation implements MessageInterface {
    public String send(String message) throws RemoteException {
        return message + "- sent!";
    }
}
