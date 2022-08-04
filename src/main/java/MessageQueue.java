import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public enum MessageQueue {
    INSTANCE;
    private final BlockingQueue<Message> messages;

    MessageQueue() {
        this.messages = new LinkedBlockingQueue<>(20);
    }
    public void addMessage(Message message){
        this.messages.add(message);
    }
    public Message getMessage() throws InterruptedException {
        return this.messages.take();
    }
}
