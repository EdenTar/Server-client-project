package bgu.spl.net.impl;

import bgu.spl.net.impl.messages.AckMessageUserInfo;
import bgu.spl.net.impl.messages.NotificationMessage;
import bgu.spl.net.impl.messages.RegisterMessage;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class User {
    private String username;
    private String password;
    private String birthday;
    private boolean isLogin;
    private LinkedList<NotificationMessage> notificationMessagesList;
    private LinkedList<String> followersList;
    private LinkedList<String> followingList;
    private LinkedList<String> blockedUsers;
    private short followersNum;
    private short followingNum;
    private short postsNum;
    private AckMessageUserInfo userInfo;

    public User(RegisterMessage registerMessage){
        username = registerMessage.getUsername();
        password = registerMessage.getPassword();
        birthday = registerMessage.getBirthday();
        isLogin = false;
        notificationMessagesList = new LinkedList<NotificationMessage>();
        followersList = new LinkedList<>();
        followingList = new LinkedList<>();
        blockedUsers=new LinkedList<>();
        followersNum = 0;
        followingNum = 0;
        postsNum = 0;

        userInfo=new AckMessageUserInfo(getAge(),postsNum,followersNum,followingNum);
    }


    public void logout(){
        isLogin = false;
    }
    /*public void setPostsNum(int num){
       // postsNum = num;
    }*/
    public void decPostsNum(){
        postsNum++;
        userInfo.setNumPosts(postsNum);
    }
    public short getPostsNum(){return postsNum;}

    public short getAge(){
        System.out.println(birthday);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
        LocalDate dateBirthday = LocalDate.parse(birthday, formatter);

       //LocalDate localDateBirthday = dateBirthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        if ((dateBirthday != null) && (currentDate != null)) {
            return (short)Period.between(dateBirthday, currentDate).getYears();
        }
        return 0;

        //return (short)(Year.now().getValue()-Integer.parseInt(birthday.substring(5)));
    }
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public boolean getIsLogin(){return isLogin;}
    public short getFollowersNum(){return followersNum;}
    public short getFollowingNum(){return followingNum;}

    public LinkedList<NotificationMessage> getNotificationMessagesList(){return notificationMessagesList;}
    public void deleteNotificationMessagesList(){notificationMessagesList = new LinkedList<NotificationMessage>();}
    public LinkedList<String> getFollowingList(){return followingList;}
    public LinkedList<String> getFollowersList(){return followersList;}
    public void setLogin(){isLogin=!isLogin;}
    public LinkedList<String> getBlockedUsers(){return blockedUsers;}
    public void increaseFollowingNum(){
        followingNum++;
        userInfo.setNumFollowing(followingNum);
    }
    public void decreaseFollowingNum(){
        followingNum--;
        userInfo.setNumFollowing(followingNum);
    }

    public void increaseFollowersNum(){
        followersNum++;
        userInfo.setNumFollowers(followersNum);
    }
    public void decreaseFollowersNum(){
        followersNum--;
        userInfo.setNumFollowers(followersNum);
    }


}
