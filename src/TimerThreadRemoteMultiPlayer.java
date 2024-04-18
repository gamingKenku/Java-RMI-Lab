import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class TimerThreadRemoteMultiPlayer extends TimerThread {
    protected final WordsGameMultiPlayerInterface __game_stub;
    protected final PlayerInterface __player;

    public TimerThreadRemoteMultiPlayer(int seconds_to_answer, WordsGameMultiPlayerInterface game_stub, PlayerInterface player_stub) {
        super(seconds_to_answer);
        __game_stub = game_stub;
        __player = player_stub;
    }

    @Override
    public void run() {
        try {
            int seconds = __seconds_to_answer;
            seconds /= 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            __player.Notify(seconds + " секунд осталось...");
            seconds = seconds / 3 * 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            __player.Notify(seconds + " секунд осталось...");
            seconds /= 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            __player.Notify(seconds + " секунд осталось...");
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            __player.Notify("Время вышло! Введите любую строку для завершения.");
        }
        catch (InterruptedException ex) {
        } catch (RemoteException ex) {
            System.out.println("Failed finding the player.");
        }
    }
}
