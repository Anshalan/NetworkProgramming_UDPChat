import java.time.LocalDateTime;
import java.util.UUID;

public class Message {
    UUID senderId;
    String senderName;
    MessageType messageType;
    LocalDateTime sendDate;
    String message;
    UUID targetId;
    Room room;

    public Message(UUID senderId, String senderName,MessageType messageType, LocalDateTime sendDate, String message, Room room) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.messageType = messageType;
        this.sendDate = sendDate;
        this.message = message;
        this.room = room;
    }
    public Message(UUID senderId, String senderName,MessageType messageType, LocalDateTime sendDate, String message, Room room, UUID targetId) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.messageType = messageType;
        this.sendDate = sendDate;
        this.message = message;
        this.room = room;
        this.targetId = targetId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public String getMessage() {
        return message;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public Room getRoom() {
        return room;
    }
}
