import sun.rmi.runtime.Log;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by uoles on 26.04.2016.
 */

public class Client implements Runnable {

    public Client() {
        new Thread(this, "Client").start();
    }

    @Override
    public void run() {
        try {
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
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
