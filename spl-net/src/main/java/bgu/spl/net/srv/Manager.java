package bgu.spl.net.srv;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.impl.User;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.impl.messages.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Manager {


    private ConcurrentHashMap<Integer,User> connectionID_users;
    private ConcurrentHashMap<String , User> registerUsers;
    private LinkedList<Message> pmAndPostMessages;
    private ConnectionsImpl connections;
    private List<String> badWords;

   private static class SingeltonHolderManager{
        private static Manager instance = new Manager();
    }

    private Manager(){
        connectionID_users = new ConcurrentHashMap<Integer, User>();
        registerUsers = new ConcurrentHashMap<String, User>();
        pmAndPostMessages = new LinkedList<>();
        badWords= Arrays.asList("hate","bad","anger","anxiety","war");
    }

    public static Manager getInstance(){
        return SingeltonHolderManager.instance;
    }


    public void registerRequest(RegisterMessage registerMessage, int connectionId){
        Message returnMessage;
        if( registerUsers.containsKey(registerMessage.getUsername()))
        {
            returnMessage = new ErrorMessage(registerMessage.getOpCode());
        }
        else
        {
            User user = new User(registerMessage);
            registerUsers.put(registerMessage.getUsername(),user);
            returnMessage = new AckMessage(registerMessage.getOpCode());
        }
        connections.send(connectionId,returnMessage);
    }

    public void loginRequest(LoginMessage loginMessage, int connectionId){
        if(!registerUsers.containsKey(loginMessage.getUsername()))
            connections.send(connectionId,new ErrorMessage((short)2));
        else if(!registerUsers.get(loginMessage.getUsername()).getPassword().equals(loginMessage.getPassword()))
            connections.send(connectionId,new ErrorMessage((short)2));
        else if(registerUsers.get(loginMessage.getUsername()).getIsLogin())
            connections.send(connectionId,new ErrorMessage((short)2));
        else if(loginMessage.getCaptcha()=='0')
            connections.send(connectionId,new ErrorMessage((short)2));
        else {
            registerUsers.get(loginMessage.getUsername()).setLogin();
            connections.send(connectionId, new AckMessage((short) 2));
            User user = registerUsers.get(loginMessage.getUsername());
            connectionID_users.put(connectionId, user);
            registerUsers.put(loginMessage.getUsername(), connectionID_users.get(connectionId));
            LinkedList<NotificationMessage> notificationMessages = registerUsers.get(loginMessage.getUsername()).getNotificationMessagesList();
            for (NotificationMessage notificationMessage : notificationMessages) {
                connections.send(connectionId, notificationMessage);
            }
            registerUsers.get(loginMessage.getUsername()).deleteNotificationMessagesList();
        }

    }

    public void logoutRequest(LogoutMessage logoutMessage, int connectionId){
        Message returnMessage;
        if (!connectionID_users.containsKey(connectionId) || !connectionID_users.get(connectionId).getIsLogin() )
        {
            returnMessage = new ErrorMessage(logoutMessage.getOpCode());
        }
        else
        {
            connectionID_users.get(connectionId).logout();
            connectionID_users.remove(connectionId);
            returnMessage = new AckMessage(logoutMessage.getOpCode());
        }

        connections.send(connectionId,returnMessage);
    }

    public void follow_unfollowRequest(Follow_UnfollowMessage follow_unfollowMessage, int connectionId){
        if(!connectionID_users.get(connectionId).getIsLogin())
            connections.send(connectionId,new ErrorMessage((short)4));
        else if (registerUsers.get(follow_unfollowMessage.getUsername())==null)
            connections.send(connectionId,new ErrorMessage((short)4));
        else if(follow_unfollowMessage.getFollow()==0){
            if(connectionID_users.get(connectionId).getFollowingList().contains(follow_unfollowMessage.getUsername()))
                connections.send(connectionId,new ErrorMessage((short)4));
            else {
                //adds to the following list
                connectionID_users.get(connectionId).getFollowingList().add(follow_unfollowMessage.getUsername());
                connectionID_users.get(connectionId).increaseFollowingNum();
                //adds to the followers list
                registerUsers.get(follow_unfollowMessage.getUsername()).getFollowersList().add(connectionID_users.get(connectionId).getUsername());
                registerUsers.get(follow_unfollowMessage.getUsername()).increaseFollowersNum();
                connections.send(connectionId,new AckFollow_UnfollowMessage(follow_unfollowMessage.getUsername()));
            }

        }
        else if(follow_unfollowMessage.getFollow()==1){
            if(!connectionID_users.get(connectionId).getFollowingList().contains(follow_unfollowMessage.getUsername()))
                connections.send(connectionId,new ErrorMessage((short)4));
            else {
                //removes from the following list
                connectionID_users.get(connectionId).getFollowingList().remove(follow_unfollowMessage.getUsername());
                connectionID_users.get(connectionId).decreaseFollowingNum();

                //removes from the followers list
                registerUsers.get(follow_unfollowMessage.getUsername()).getFollowersList().remove(connectionID_users.get(connectionId));
                registerUsers.get(follow_unfollowMessage.getUsername()).decreaseFollowersNum();
                connections.send(connectionId,new AckFollow_UnfollowMessage(follow_unfollowMessage.getUsername()));
            }
        }
    }

    public void postRequest (PostMessage postMessage, int connectionId){
        if (!connectionID_users.containsKey(connectionId) || !connectionID_users.get(connectionId).getIsLogin())
           connections.send(connectionId,new ErrorMessage((short)5));
        else {
            NotificationMessage returnMessage = new NotificationMessage('1', connectionID_users.get(connectionId).getUsername(),postMessage.getContent());
            //for each unregister user - save notification
            LinkedList<String> usernameRec = connectionID_users.get(connectionId).getFollowersList();
            String[] usernames = postMessage.getContent().split("@");
            for(int i=1 ; i<usernames.length ; i++)
            {

                    String[] usernamess = usernames[i].split(" ");
                User user = registerUsers.get(usernamess[0]);
                    if (!usernameRec.contains(usernamess[0]) &&
                            !connectionID_users.get(connectionId).getBlockedUsers().contains(user.getUsername()) &&
                            !registerUsers.get(user.getUsername()).getBlockedUsers().contains(connectionID_users.get(connectionId).getUsername()))
                            usernameRec.add(usernamess[0]);

            }
            for(String username : usernameRec)
            {
                if (!registerUsers.get(username).getIsLogin()){
                    registerUsers.get(username).getNotificationMessagesList().add(returnMessage);
                }
                else {
                    connections.send(getKeyByValue(username), returnMessage);
                }
            }
            pmAndPostMessages.add(postMessage);
            connectionID_users.get(connectionId).decPostsNum();
            connections.send(connectionId,new AckMessage((short)5));
        }

    }

    public void pmRequest(PMMessage pmMessage, int connectionId){
       if(connectionID_users.get(connectionId)==null)
           connections.send(connectionId,new ErrorMessage((short)6));
        else if(!connectionID_users.get(connectionId).getIsLogin())
            connections.send(connectionId,new ErrorMessage((short)6));
        else if(!registerUsers.containsKey(pmMessage.getUsername()))
            connections.send(connectionId,new ErrorMessage((short)6));
        else if(!connectionID_users.get(connectionId).getFollowingList().contains(pmMessage.getUsername()))
            connections.send(connectionId,new ErrorMessage((short)6));
        else {
            String[] split = pmMessage.getContent().split(" ");
            for (int i = 0; i < split.length; i++) {
                if (badWords.contains(split[i]))
                    split[i] = "<filtered>";
            }
            String content = "";
            for (int i = 0; i < split.length - 1; i++)
                content += split[i] + " ";
            content += split[split.length - 1];
            pmMessage.setContent(content);
            NotificationMessage notificationMessage = new NotificationMessage('0', connectionID_users.get(connectionId).getUsername(), pmMessage.getContent());
            if (registerUsers.get(pmMessage.getUsername()).getIsLogin())
                connections.send(getKeyByValue(pmMessage.getUsername()), notificationMessage);
            else {
                registerUsers.get(pmMessage.getUsername()).getNotificationMessagesList().add(notificationMessage);
            }
            pmAndPostMessages.add(pmMessage);
            connections.send(connectionId, new AckMessage((short) 6));
        }
    }

    public void logstatRequest(LogstatMessage logstatMessage, int connectionId){
        if (!connectionID_users.containsKey(connectionId) || !connectionID_users.get(connectionId).getIsLogin())
        connections.send(connectionId,new ErrorMessage(logstatMessage.getOpCode()));
        else {

            LinkedList<AckMessageUserInfo> userInfos = new LinkedList<AckMessageUserInfo>();
            for (User user : registerUsers.values()) {
                    if (!connectionID_users.get(connectionId).getBlockedUsers().contains(user.getUsername()) &&
                            !registerUsers.get(user.getUsername()).getBlockedUsers().contains(connectionID_users.get(connectionId).getUsername()))
                        userInfos.add(new AckMessageUserInfo(user.getAge(), user.getPostsNum(), user.getFollowersNum(), user.getFollowingNum()));

            }
            connections.send(connectionId, new AckLogstat(logstatMessage.getOpCode(), userInfos)); //build userInfo linkedList
        }
    }

    public void statRequest(StatsMessage statsMessage, int connectionId) {
       boolean send=false;
       if(connectionID_users.get(connectionId)==null) {
           connections.send(connectionId, new ErrorMessage((short) 8));
           send=true;
       }
       if (!send && !connectionID_users.get(connectionId).getIsLogin() ) {
           connections.send(connectionId, new ErrorMessage((short) 8));
           send=true;
       }
       if (!send && !registerUsers.containsKey(connectionID_users.get(connectionId).getUsername())) {
           connections.send(connectionId, new ErrorMessage((short) 8));
           send=true;
       }
       if(!send){
            for(int i=0;i<statsMessage.getUsernames().size();i++){
                if(!send && !registerUsers.containsKey(statsMessage.getUsernames().get(i))) {
                    connections.send(connectionId, new ErrorMessage((short) 8));
                    send=true;
                }
                else if(!send && connectionID_users.get(connectionId).getBlockedUsers().contains(statsMessage.getUsernames().get(i))) {
                    statsMessage.getUsernames().remove(statsMessage.getUsernames().get(i));
                }

                else if(!send && registerUsers.get(statsMessage.getUsernames().get(i)).getBlockedUsers().contains(connectionID_users.get(connectionId).getUsername())) {
                    statsMessage.getUsernames().remove(statsMessage.getUsernames().get(i));
                }
            }
       }
        if(!send) {
            LinkedList<AckMessageUserInfo> userInfos = new LinkedList<AckMessageUserInfo>();
            for (int i = 0; i < statsMessage.getUsernames().size(); i++) {
                User user = registerUsers.get(statsMessage.getUsernames().get(i));
                userInfos.add(new AckMessageUserInfo(user.getAge(), user.getPostsNum(), user.getFollowersNum(), user.getFollowingNum()));
            }
            connections.send(connectionId, new AckStatMessage(statsMessage.getOpCode(), userInfos));//build userInfo linkedList
            send=true;

        }
    }
    public  void blockRequest(BlockMessage blockMessage, int connectionId){
        if(connectionID_users.get(connectionId)==null)
           connections.send(connectionId,new ErrorMessage((short)12));
        else if(!registerUsers.containsKey(blockMessage.getUsername()))
            connections.send(connectionId,new ErrorMessage((short)12));
        else {
            if (connectionID_users.get(connectionId).getFollowersList().contains(blockMessage.getUsername())) {
                connectionID_users.get(connectionId).decreaseFollowersNum();
                registerUsers.get(blockMessage.getUsername()).decreaseFollowingNum();
            }
            if(connectionID_users.get(connectionId).getFollowingList().contains(blockMessage.getUsername())) {
                connectionID_users.get(connectionId).decreaseFollowingNum();
                registerUsers.get(blockMessage.getUsername()).decreaseFollowersNum();
            }
            connectionID_users.get(connectionId).getBlockedUsers().add(blockMessage.getUsername());
            connectionID_users.get(connectionId).getFollowingList().remove(blockMessage.getUsername());
            connectionID_users.get(connectionId).getFollowersList().remove(blockMessage.getUsername());
            registerUsers.get(blockMessage.getUsername()).getFollowingList().remove(connectionID_users.get(connectionId).getUsername());
            registerUsers.get(blockMessage.getUsername()).getFollowersList().remove(connectionID_users.get(connectionId).getUsername());

            connections.send(connectionId, new AckMessage((short) 12));
        }
    }


    public Message notificationRequest(NotificationMessage notificationMessage){
        return null;
    }
    public  Message ackRequest(AckMessage ackMessage){
        return null;
    }
    public  Message ErrorRequest(ErrorMessage errorMessage){
        return null;
    }
    public void setConnections(ConnectionsImpl connections){
       this.connections=connections;
    }
    private Integer getKeyByValue(String user) {
        for(Integer id :connectionID_users.keySet()) {
            if (connectionID_users.get(id).getUsername().equals(user))
                return id;
        }
        return null;
    }

}
