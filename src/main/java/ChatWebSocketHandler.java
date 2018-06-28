import netscape.javascript.JSObject;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.json.JSONObject;

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("User connected");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        if(Chat.sessionIDs.containsValue(user)){
            String username = Chat.sessionIDs.inverse().get(user);
            Chat.sessionIDs.inverse().remove(user);
            Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println(message);;
        if(Chat.sessionIDs.containsValue(user)){
            Chat.broadcastMessage(sender = Chat.sessionIDs.inverse().get(user), msg = message);
        }
        else {
            if(!Chat.sessionIDs.containsKey(message)){
                System.out.println("Added user: " + message);
                Chat.sessionIDs.put(message, user);
            }else {
                Chat.sessionIDs.get(message).close();
                Chat.sessionIDs.forcePut(message, user);
                System.err.println("ERROR, OVERLAPPED ID'S");
            }
        }
    }
}
