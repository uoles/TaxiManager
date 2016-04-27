import sun.rmi.runtime.Log;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by uoles on 26.04.2016.
 */

public class Client implements Runnable {

    public Client() {
        run();
    }

    @Override
    public void run() {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() {
        int id = 0;
        try {
            while (id > -1) {
                id = Dispetcher.getInstance().setMessage(Message.createXML());
                if (id > 0) {
                    System.out.println(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
