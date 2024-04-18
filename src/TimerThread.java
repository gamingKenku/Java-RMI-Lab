import java.util.concurrent.TimeUnit;

class TimerThread implements Runnable {
    protected final int __seconds_to_answer;

    public TimerThread(int seconds_to_answer) {
        __seconds_to_answer = seconds_to_answer;
    }

    public void run() {
        try {
            int seconds = __seconds_to_answer;
            seconds /= 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            System.out.println(seconds + " секунд осталось...");
            seconds = seconds / 3 * 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            System.out.println(seconds + " секунд осталось...");
            seconds /= 2;
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            System.out.println(seconds + " секунд осталось...");
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
            System.out.println("Время вышло! Введите любую строку для завершения.");
        }
        catch (InterruptedException ex) {
        }
    }
}