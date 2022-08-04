package communication;

import model.*;
import utils.MessageParser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receiver implements Runnable {
	private final String address;
	private final MulticastSocket socket;
	private final byte[] buffer;
	private final MessageQueue messageQueue;
	private final User user;
	private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	public Receiver(String address, Integer port) throws IOException {
		this.socket = new MulticastSocket(port);
		this.address = address;
		this.buffer = new byte[1024];
		this.messageQueue = MessageQueue.INSTANCE;
		this.user = User.INSTANCE;
	}

	@Override
	public void run() {
		try {
			socket.joinGroup(InetAddress.getByName(address));
			while (true) {
				Message message = this.getMessage(socket);
				if (message.getMessageType().equals(MessageType.NICK_CHECK)) {
					handleNickCheck(message);
				} else if (message.getRoom().equals(this.user.getRoom())) {
					switch (message.getMessageType()) {
						case NICK_BUSY:
							handleNickBusy(message);
							break;
						case JOIN_ROOM:
							handleJoinRoom(message);
							break;
						case LEAVE_ROOM:
							handleLeaveRoom(message);
							break;
						case MESSAGE:
							handleMessage(message);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Message getMessage(MulticastSocket socket) throws IOException {
		DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
		socket.receive(pack);
		return MessageParser.parseFromJson(new String(pack.getData(), 0, pack.getLength()));
	}

	private void handleNickCheck(Message message) {
		if (user.getName() != null && !user.getId().equals(message.getSenderId())) {
			if (user.getName().equals(message.getSenderName())) {
				Message nickUsedMessage = new Message(user.getId(), user.getName(), MessageType.NICK_BUSY, LocalDateTime.now(),
						String.format("\"%s\" already in use", message.getSenderName()), Room.GENERAL, message.getSenderId());
				messageQueue.addMessage(nickUsedMessage);
			}
		}
	}

	private void handleNickBusy(Message message) {
		if (message.getTargetId() != null && this.user.getId().equals(message.getTargetId())) {
			this.user.clearUser();
			System.out.println(message.getMessage());
		}
	}

	private void handleJoinRoom(Message message) {
		if (user.getName() != null) {
			if (user.getId().equals(message.getSenderId())) {
				System.out.printf("You have joined room %s%n", message.getRoom());
			} else {
				System.out.printf("model.User \"%s\" joined room %s%n", message.getSenderName(), message.getRoom());
			}
		}
	}

	private void handleLeaveRoom(Message message) {
		if (user.getName() != null) {
			if (!user.getId().equals(message.getSenderId())) {
				System.out.printf("model.User \"%s\" left room %s%n", message.getSenderName(), message.getRoom());
			}
		}
	}

	private void handleMessage(Message message) {
		System.out.printf("[%s] %s: %s%n", message.getSendDate().format(dateFormat), message.getSenderName(), message.getMessage());
	}
}
