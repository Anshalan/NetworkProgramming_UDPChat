import com.google.gson.Gson;

public class MessageParser {
    private static final Gson gson = new Gson();
    private MessageParser(){}
    public static String parseToJson(Message message){
        return gson.toJson(message);
    }
    public static Message parseFromJson(String json){
        return gson.fromJson(json, Message.class);
    }


}