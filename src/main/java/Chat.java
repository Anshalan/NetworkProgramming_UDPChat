import communication.Receiver;
import communication.Sender;
import terminal.Terminal;

import java.io.IOException;

//$changeroom <roomname> - changes room <to one with given name (defined in enum Room)
//$leave - leaves current room and joins GENERAL or exits if current room is GENERAL
public class Chat {
	public static void main(String[] args) throws IOException {
		Thread sender = new Thread(new Sender("230.0.0.0", 8888), "Sender-Thread");
		Thread receiver = new Thread(new Receiver("230.0.0.0", 8888), "Receiver-Thread");
		Thread terminal = new Thread(new Terminal(), "Terminal-Thread");
		sender.start();
		receiver.start();
		terminal.start();
	}
}
