import java.time.LocalDateTime;
import java.util.Scanner;

public class Terminal implements Runnable {

    private final MessageQueue messageQueue;
    private final User user;

    public Terminal() {
        this.messageQueue = MessageQueue.INSTANCE;
        this.user = User.INSTANCE;
    }

    @Override
    public void run() {
        System.out.println("Welcome to UDP Chat");
        Scanner scanner = new Scanner(System.in);
        getAndSetUserName(scanner);
        messageQueue.addMessage(new Message(this.user.getId(), this.user.getName(), MessageType.JOIN_ROOM, LocalDateTime.now(), this.user.getName(), user.getRoom()));
        System.out.println("Your name has been set as: " + this.user.getName());
        while (true) {
            handleCommand(scanner);
        }
    }

    private void handleMessage(String userMessage) {
        Message message = new Message(user.getId(), user.getName(), MessageType.MESSAGE, LocalDateTime.now(), userMessage, user.getRoom());
        messageQueue.addMessage(message);
    }

    //name.length 3-16
    //A-Z a-z 0-9
    private boolean verifyUsername(String name) {
        String regex = "^\\w{3,16}$";
        return name.matches(regex);
    }

    private void getAndSetUserName(Scanner scanner) {
        String username;
        while (this.user.getName() == null) {
            System.out.println("Please, enter your name");
            username = scanner.nextLine();
            if (!verifyUsername(username)) {
                System.out.println("Given username is not valid");
                continue;
            }
            user.setUser(username);
            Message message = new Message(this.user.getId(), this.user.getName(), MessageType.NICK_CHECK, LocalDateTime.now(), this.user.getName(), user.getRoom());
            this.messageQueue.addMessage(message);
            System.out.printf("Please wait, verifying if username \"%s\" is available%n", username);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleCommand(Scanner scanner) {
        String userMessage = scanner.nextLine();
        if (userMessage.startsWith("$")) {
            String[] messageElements = userMessage.replace("$", "").trim().split(" ");
            if(messageElements.length>0){
                if (messageElements[0].equalsIgnoreCase("changeroom")) {
                    handleChangeRoom(messageElements);
                } else if (messageElements[0].equalsIgnoreCase("leave")) {
                    handleLeave(userMessage);
                }else{
                    System.out.println("Incorrect command");
                }
            }else{
                System.out.println("Missing command");
            }

        } else {
            handleMessage(userMessage);
        }
    }

    private void handleChangeRoom(String[] messageArray) {
        String userMessage;
        Room room = Room.valueOf(messageArray[2].toUpperCase());
        try {
            userMessage = room.name();
            Message message = new Message(user.getId(), user.getName(), MessageType.LEAVE_ROOM, LocalDateTime.now(), userMessage, user.getRoom());
            messageQueue.addMessage(message);
            this.user.changeRoom(room);
            message = new Message(user.getId(), user.getName(), MessageType.JOIN_ROOM, LocalDateTime.now(), userMessage, user.getRoom());
            messageQueue.addMessage(message);
        } catch (IllegalArgumentException e) {
            System.out.printf("There is no room \"%s\"%n", room);
        }
    }

    private void handleLeave(String userMessage) {
        Message message = new Message(user.getId(), user.getName(), MessageType.LEAVE_ROOM, LocalDateTime.now(), userMessage, user.getRoom());
        messageQueue.addMessage(message);
        if (user.getRoom().equals(Room.GENERAL)) {
            this.user.changeRoom(null);
            System.exit(0);
        } else {
            this.user.changeRoom(Room.GENERAL);
            message = new Message(user.getId(), user.getName(), MessageType.JOIN_ROOM, LocalDateTime.now(), userMessage, user.getRoom());
            messageQueue.addMessage(message);
        }
    }
}
