import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GameServerMultiPlayer {
    public GameServerMultiPlayer() {

    }

    public static void main(String[] arg) {
        try {
            WordsGameMultiPlayer game = new WordsGameMultiPlayer("out\\production\\Java-RMI-Lab\\cities.txt");
            WordsGameMultiPlayerInterface game_stub = (WordsGameMultiPlayerInterface) UnicastRemoteObject.exportObject(game, 0);
            Registry registry = LocateRegistry.getRegistry();

            try {
                registry.unbind("Game");
                registry.bind("Game", game_stub);
            } catch(NotBoundException ex) {
                registry.bind("Game", game_stub);
            }

            System.err.println("Server ready");
        } catch (Exception ex) {
            System.err.println("Server exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
