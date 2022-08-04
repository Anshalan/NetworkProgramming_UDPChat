package communication;

import model.Message;
import utils.MessageParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Sender implements Runnable {

	private final String address;
	private final Integer port;
	private final MessageQueue messageQueue;

	public Sender(String address, Integer port) {
		this.address = address;
		this.port = port;
		this.messageQueue = MessageQueue.INSTANCE;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Message message = this.messageQueue.getMessage();
				DatagramSocket socket = new DatagramSocket();
				byte[] buffer = MessageParser.parseToJson(message).getBytes(StandardCharsets.UTF_8);
				DatagramPacket pack = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.address), this.port);
				socket.send(pack);
				socket.close();
			} catch (InterruptedException | IOException e) {
				System.out.println("Something went wrong: " + e.getMessage());
				throw new RuntimeException(e);
			}
		}
	}
}
