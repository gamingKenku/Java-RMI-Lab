import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class PlayerMulti implements Serializable, PlayerInterface {
    private final String __name;
    private final WordsGameMultiPlayerInterface __game_stub;
    private final Scanner __sc;

    public PlayerMulti(String name, WordsGameMultiPlayerInterface game_stub) {
        __name = name;
        __game_stub = game_stub;
        __sc = new Scanner(System.in);
    }

    @Override
    public void RequestWord() throws RemoteException {
        if (__game_stub.GetLastWord() != null) {
            System.out.println("Ваша очередь! Последнее слово: " + Character.toUpperCase(__game_stub.GetLastWord().charAt(0)) + __game_stub.GetLastWord().substring(1) + ".");
        } else {
            System.out.println("Ваша очередь! Это будет первый город.");
        }

        System.out.println("Вы можете выйти, напечатав \"END\".");

        WordsGameMultiPlayerInterface.ValidationResponse response = null;
        while (response != WordsGameMultiPlayerInterface.ValidationResponse.PASSED && response != WordsGameMultiPlayerInterface.ValidationResponse.TIME_OUT && response != WordsGameMultiPlayerInterface.ValidationResponse.FORCE_EXIT) {
            String word = __sc.nextLine().toLowerCase();
            response = __game_stub.SubmitWord(word);

            switch (response) {
                case NO_INPUT:
                    System.out.println("Введите название города.");
                    break;
                case PASSED:
                    break;
                case TIME_OUT:
                    System.out.println("Время вышло. Игра окончена.");
                    UnicastRemoteObject.unexportObject(this, true);
                    break;
                case WORD_ALREADY_CALLED:
                    System.out.println("Город уже был назван.");
                    break;
                case WORD_NOT_FOUND:
                    System.out.println("Такого города не существует.");
                    break;
                case WRONG_FIRST_LETTER:
                    System.out.println("Название города не начинается на букву, на которую заканчивается название последнего города: " + Character.toUpperCase(__game_stub.GetLastWord().charAt(0)) + __game_stub.GetLastWord().substring(1) + ".");
                    break;
                case FORCE_EXIT:
                    System.out.println("Удачного дня!");
                    UnicastRemoteObject.unexportObject(this, true);
                    break;
            }
        }
    }

    @Override
    public String GetName() throws RemoteException {
        return __name;
    }

    @Override
    public void Notify(String message) throws RemoteException {
        System.out.println(message);
    }
}
