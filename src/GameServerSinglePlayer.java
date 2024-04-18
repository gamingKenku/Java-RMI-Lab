import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GameServerSinglePlayer {
    public GameServerSinglePlayer() {

    }

    public static void main(String[] arg) {
        try {
            WordsGameSinglePlayer game = new WordsGameSinglePlayer("out\\production\\Java-RMI-Lab\\cities.txt");
            WordsGameSinglePlayerInterface game_stub = (WordsGameSinglePlayerInterface) UnicastRemoteObject.exportObject(game, 0);
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
