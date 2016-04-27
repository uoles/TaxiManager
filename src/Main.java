import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

/**
 * Created by uoles on 26.04.2016.
 */

public class Main {

    public static void main(String[] args) {

        // XML записываются в корень проекта, в папку Test
        int K = 9;
        int N = 99;
        int messagesCount = 1000;

        Dispetcher.getInstance().init(K, messagesCount);
        for (int i = 0; i < N; i++) {
            new Client();
        }

    }
}
