import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class TimerThread implements Runnable {
    private int __seconds_to_answer;

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
            System.out.println("Время вышло!");
        }
        catch (InterruptedException ex) {
        }
    }
}

public class WordsGameLocal {
    private ArrayList<String> __cities;
    private ArrayList<String> __used_cities;
    private String __last_city;
    private final int __seconds_to_answer = 30;

    public WordsGameLocal(String cities_path) {
        __last_city = null;
        __cities = new ArrayList<>();
        __used_cities = new ArrayList<>();
        
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

    public void Start() {
        System.out.println("Игра началась! Напишите \"END\" для отмены или что-нибудь другое для продолжения.");
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();

        if (command.equals("END")) {
            System.out.println("Игра отменена!");
            sc.close();
            return;
        }
        
        __last_city = null;
        __used_cities.clear();
        RequestWord(sc);
        sc.close();
    }

    private void RequestWord(Scanner sc) {
        Thread timerThread = new Thread(new TimerThread(__seconds_to_answer));
        System.out.println("Таймер пошёл, называйте город!");
        
        timerThread.start();
        while (true) {
            String word = sc.nextLine();

            if (word.isEmpty()) {
                System.out.println("Введите строку.");
                continue;
            }

            if (!__cities.contains(word)) {
                System.out.println("Город не существует, попробуйте снова!");
                continue;
            }

            if (__used_cities.contains(word)) {
                System.out.println("Город уже был назван, попробуйте снова!");
                continue;
            }

            if (__last_city == null) {
                __last_city = word;
                __used_cities.add(word);
                break;
            }

            if (word.charAt(0) != __last_city.charAt(__last_city.length() - 1)) {
                System.out.println("Название города не начинается на букву, на которую заканчивается название последнего города: " + __last_city);
                continue;
            }

            __last_city = word;
            __used_cities.add(word);
            break;
        }

        if (timerThread.isAlive()) {
            System.out.println("Таймер истёк. Игра окончена.");
        };
        timerThread.interrupt();

        RequestWord(sc);
    }
}
