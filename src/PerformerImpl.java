/**
 * Created by uoles on 26.04.2016.
 */
public class PerformerImpl implements IPerformer {

    private boolean status = true;

    public synchronized boolean isFree() {
        return status;
    }

    public synchronized void saveMessage(Message message) {
        status = false;
        try {
            message.saveXML();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            delay();
            status = true;
        }
    }

    public void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
