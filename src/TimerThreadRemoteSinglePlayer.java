import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class TimerThreadRemoteSinglePlayer extends TimerThread {
    protected final WordsGameSinglePlayerInterface __game_stub;

    public TimerThreadRemoteSinglePlayer(int seconds_to_answer, WordsGameSinglePlayerInterface game_stub) {
        super(seconds_to_answer);
        __game_stub = game_stub;
    }

    @Override
    public void run() {
        try {
            PlayerInterface player_stub = __game_stub.GetPlayerStub();
            int seconds = __seconds_to_answer;
            seconds /= 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            player_stub.Notify(seconds + " секунд осталось...");
            seconds = seconds / 3 * 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            player_stub.Notify(seconds + " секунд осталось...");
            seconds /= 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            player_stub.Notify(seconds + " секунд осталось...");
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            player_stub.Notify("Время вышло! Введите любую строку для завершения.");
        }
        catch (InterruptedException ex) {
        } catch (RemoteException ex) {
            System.out.println("Failed finding the player.");
        }
    }
}
