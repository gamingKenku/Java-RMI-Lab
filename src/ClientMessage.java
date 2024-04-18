import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMessage {
    private ClientMessage() {
    }

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            MessageInterface stub = (MessageInterface) registry.lookup("Sending");
            String response = String.valueOf(stub.send("Hello World!"));
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}