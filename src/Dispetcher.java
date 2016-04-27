import java.util.ArrayList;

/**
 * Created by uoles on 26.04.2016.
 */

public class Dispetcher {

    private static volatile Dispetcher instance;
    private ArrayList<IPerformer> performers;
    private ArrayList<IPerformer> messages;
    private int id = 0;
    private int messagesCount;

    private Dispetcher() {
    }

    public void init(int performersCount, int messageCount) {
        this.messagesCount = messageCount;
        performers = new ArrayList<>();
        for (int i = 0; i < performersCount; i++) {
            performers.add(new PerformerImpl());
        }
    }

    public static Dispetcher getInstance() {
        if (instance == null) {
            synchronized (Dispetcher.class) {
                if (instance == null) {
                    instance = new Dispetcher();
                }
            }
        }
        return instance;
    }

    public int setMessage(String xmlMessage) {
        int dispatchedId = getDispatchedId();
        if (dispatchedId > -1) {
            new Message(dispatchedId, xmlMessage, performers);
        }
        return dispatchedId;
    }

    private int getDispatchedId() {
        if (id < messagesCount) {
            return ++id;
        }
        return -1;
    }
}
