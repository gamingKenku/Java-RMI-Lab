import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;


public class WordsGameSinglePlayer implements WordGameSinglePlayerInterface {
    private final WordsGameLocal __g_data;

    public WordsGameSinglePlayer(String path) {
        __g_data = new WordsGameLocal(path);
    }

    @Override
    public boolean SubmitWord(String word) {
        return true;
    }
}
