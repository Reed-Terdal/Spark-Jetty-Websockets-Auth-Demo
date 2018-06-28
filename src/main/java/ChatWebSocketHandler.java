import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.common.WebSocketSession;

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        System.out.println("User connected");
//        System.out.println(user.getLocalAddress());
//        System.out.println(user.toString());
//        WebSocketSession session = (WebSocketSession) user;

//        String username = "User" + Chat.nextUserNumber++;
//        Chat.userUsernameMap.put(user, username);
//        Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        if(Chat.sessionIDs.containsValue(user)){
            String username = Chat.sessionIDs.inverse().get(user);
            Chat.sessionIDs.inverse().remove(user);
            Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
        }
//        String username = Chat.userUsernameMap.get(user);
//        Chat.userUsernameMap.remove(user);
//        Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println(user.getLocalAddress());
        if(Chat.sessionIDs.containsValue(user)){
            Chat.broadcastMessage(sender = Chat.sessionIDs.inverse().get(user), msg = message);
        }
        else {
            if(!Chat.sessionIDs.containsKey(message)){
                System.out.println("Added user: " + message);
                Chat.sessionIDs.put(message, user);
            }else {
                System.err.println("ERROR, OVERLAPPED ID'S");
            }
        }
//        Chat.broadcastMessage(sender = Chat.userUsernameMap.get(user), msg = message);

    }

}
