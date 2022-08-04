import java.time.LocalDateTime;
import java.util.UUID;

public enum User {
    INSTANCE;
    private UUID id;
    private String name;
    private LocalDateTime joinDate;
    private Room room;

    User() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setUser(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.joinDate = LocalDateTime.now();
        this.room = Room.GENERAL;
    }

    public void clearUser() {
        this.name = null;
        this.id = null;
        this.joinDate = null;
    }

    public void changeRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return this.room;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }
}
