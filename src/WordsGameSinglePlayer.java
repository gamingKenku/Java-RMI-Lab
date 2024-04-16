import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;

public class WordsGameSinglePlayer implements WordGameSinglePlayerInterface {
    private final Thread __timer_thread;
    private final ArrayList<String> __cities;
    private final ArrayList<String> __used_cities;
    private String __last_city;

    private enum ValidationResponse{
        PASSED_FIRST_WORD,
        PASSED,
        NO_INPUT,
        WORD_ALREADY_CALLED,
        WORD_NOT_FOUND,
        WRONG_FIRST_LETTER,
        TIME_OUT,
    }

    public WordsGameSinglePlayer(String cities_path) {
        __timer_thread = new Thread(new TimerThread(30));
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

    @Override
    public void Start() {

    }

    @Override
    public boolean SubmitWord(String word) {
        return true;
    }

    private ValidationResponse ValidateCity(String word) {
        if (!__timer_thread.isAlive() || word.equals("end")) return ValidationResponse.TIME_OUT;
        if (word.isEmpty()) return ValidationResponse.NO_INPUT;
        if (!__cities.contains(word)) return ValidationResponse.WORD_NOT_FOUND;
        if (__used_cities.contains(word)) return ValidationResponse.WORD_ALREADY_CALLED;
        if (__last_city == null) return ValidationResponse.PASSED_FIRST_WORD;
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
}
