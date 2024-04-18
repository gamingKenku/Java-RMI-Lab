import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class GameClientSinglePlayer {
    public GameClientSinglePlayer() {

    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        WordsGameSinglePlayerInterface game_stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            game_stub = (WordsGameSinglePlayerInterface) registry.lookup("Game");
            System.out.println("Подключение установлено.");
        } catch (Exception ex) {
            System.out.println("Ошибка при подключении.");
            ex.printStackTrace();
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите своё имя.");
        String name = sc.nextLine();
        PlayerSingle player = new PlayerSingle(name, game_stub);

        try {
            PlayerInterface player_stub = (PlayerInterface) UnicastRemoteObject.exportObject(player, 0);
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind("Player");
                registry.bind("Player", player_stub);
            } catch(NotBoundException ex) {
                registry.bind("Player", player_stub);
            }
            game_stub.RegisterPlayer(player_stub);
            System.out.println("Регистрация прошла успешно.");
            game_stub.Start();
        } catch (Exception ex) {
            System.out.println("Ошибка при подключении.");
            ex.printStackTrace();
        }
    }
}
