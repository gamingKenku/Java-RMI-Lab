import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordsGameSinglePlayer implements WordsGameSinglePlayerInterface {
    protected Thread __timer_thread;
    protected final ArrayList<String> __cities;
    protected final ArrayList<String> __used_cities;
    protected String __last_city;
    private PlayerInterface __player;

    public WordsGameSinglePlayer(String cities_path) {
        __last_city = null;
        __cities = new ArrayList<>();
        __used_cities = new ArrayList<>();
        __player = null;
        try (FileInputStream fin = new FileInputStream(cities_path)) {
            Scanner sc = new Scanner(fin);
            while (sc.hasNextLine()) {
                __cities.add(sc.nextLine());
            }
            sc.close();
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void Start() throws RemoteException {
        __last_city = null;
        __used_cities.clear();
        __timer_thread = new Thread(new TimerThreadRemoteSinglePlayer(30, this));
        __timer_thread.start();
        __player.RequestWord();
    }

    @Override
    public ValidationResponse SubmitWord(String word) {
        ValidationResponse response = ValidateCity(word);

        if (response == ValidationResponse.PASSED) {
            __timer_thread.interrupt();
            __last_city = word;
            __used_cities.add(word);
            try {
                __timer_thread = new Thread(new TimerThreadRemoteSinglePlayer(30, this));
                __timer_thread.start();
                __player.Notify("Успех, названный город: " + Character.toUpperCase(__last_city.charAt(0)) + __last_city.substring(1) + ".");
                __player.RequestWord();
            } catch (RemoteException ex) {
                System.out.println("Can't request word from player.");
            }
        } else if (response == ValidationResponse.FORCE_EXIT || response == ValidationResponse.TIME_OUT) {
            try {
                DisconnectPlayer();
            } catch (RemoteException ex) {
                System.out.println("Failed to disconnect player.");
                ex.printStackTrace();
            }
        }

        return response;
    }

    protected ValidationResponse ValidateCity(String word) {
        if (word.equalsIgnoreCase("end")) return ValidationResponse.FORCE_EXIT;
        if (!__timer_thread.isAlive()) return ValidationResponse.TIME_OUT;
        if (word.isEmpty()) return ValidationResponse.NO_INPUT;
        if (!__cities.contains(word)) return ValidationResponse.WORD_NOT_FOUND;
        if (__used_cities.contains(word)) return ValidationResponse.WORD_ALREADY_CALLED;
        if (__last_city == null) return ValidationResponse.PASSED;
        if ((__last_city.charAt(__last_city.length() - 1) == 'ь' || __last_city.charAt(__last_city.length() - 1) == 'ъ')) {
            if (word.charAt(0) != __last_city.charAt(__last_city.length() - 2)) {
                return ValidationResponse.WRONG_FIRST_LETTER;
            }
        } else {
            if (word.charAt(0) != __last_city.charAt(__last_city.length() - 1)) {
                return ValidationResponse.WRONG_FIRST_LETTER;
            }
        }
        return ValidationResponse.PASSED;
    }

    @Override
    public String GetLastWord() throws RemoteException {
        return __last_city;
    }

    @Override
    public void RegisterPlayer(PlayerInterface player_stub) throws RemoteException {
        __player = player_stub;
        System.out.println("Подключенный игрок: " + __player.GetName());
    }

    private void DisconnectPlayer() throws RemoteException {
        __player = null;
        System.out.println("Игрок отключен.");
    }

    @Override
    public PlayerInterface GetPlayerStub() throws RemoteException {
        return __player;
    }
}
