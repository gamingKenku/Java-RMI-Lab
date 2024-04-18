import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

public class WordsGameMultiPlayer implements WordsGameMultiPlayerInterface {
    protected Thread __timer_thread;
    protected final ArrayList<String> __cities;
    protected final ArrayList<String> __used_cities;
    protected String __last_city;
    private ArrayList<PlayerInterface> __players;
    private Integer __current_player_index;

    public WordsGameMultiPlayer(String cities_path) {
        __last_city = null;
        __cities = new ArrayList<>();
        __used_cities = new ArrayList<>();
        __players = new ArrayList<>();
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
        Collections.shuffle(__players);
        __current_player_index = 0;
        NotifyAllPlayers("Игроки найдены, игра начинается!\nСейчас очередь " + __players.get(__current_player_index).GetName() + "!");
        __timer_thread = new Thread(new TimerThreadRemoteMultiPlayer(30, this, __players.get(__current_player_index)));
        __timer_thread.start();
        __players.get(__current_player_index).RequestWord();
    }

    @Override
    public ValidationResponse SubmitWord(String word) {
        ValidationResponse response = ValidateCity(word);

        if (response == ValidationResponse.PASSED) {
            __timer_thread.interrupt();
            __last_city = word;
            __used_cities.add(word);
            try {
                NotifyAllPlayers("Успех, названный город: " + Character.toUpperCase(__last_city.charAt(0)) + __last_city.substring(1) + ".\nСейчас очередь " + __players.get(__current_player_index).GetName() + "!");
                __timer_thread = new Thread(new TimerThreadRemoteMultiPlayer(30, this, __players.get(__current_player_index)));
                __timer_thread.start();
                __current_player_index++;
                if (__current_player_index > __players.size() - 1) __current_player_index = 0;
                __players.get(__current_player_index).RequestWord();
            } catch (RemoteException ex) {
                System.out.println("Can't request word from player.");
            }
        } else if (response == ValidationResponse.FORCE_EXIT || response == ValidationResponse.TIME_OUT) {
            try {
                DisconnectPlayer(__players.get(__current_player_index));
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
        __players.add(player_stub);
        System.out.println("Подключенный игрок: " + player_stub.GetName());

        if (__players.size() > 2) {
            Start();
        }
    }

    private void DisconnectPlayer(PlayerInterface player_stub) throws RemoteException {
        if (__players.remove(player_stub)) {
            System.out.println("Отключен игрок: " + player_stub.GetName());
        }
    }

    private void NotifyAllPlayers(String message) throws RemoteException {
        for (PlayerInterface player : __players) {
            player.Notify(message);
        }
    }
}
