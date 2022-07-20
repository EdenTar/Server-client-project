package bgu.spl.net.impl.messages;

public class AckMessageUserInfo {
    private short age;
    private short numPosts;
    private short numFollowers;
    private short numFollowing;

    public AckMessageUserInfo(short age,short numPosts,short numFollowers,short numFollowing){
        this.age=age;
        this.numPosts=numPosts;
        this.numFollowers=numFollowers;
        this.numFollowing=numFollowing;
    }

    public String getNumPosts(){
        if (numPosts<10)
            return "0"+numPosts;
        return ""+numPosts;
    }
    public String getAge(){
        if (age<10)
            return "0"+age;
        return ""+age;
    }
    public String getNumFollowers(){
        if (numFollowers<10)
            return "0"+numFollowers;
        return ""+numFollowers;}
    public String getNumFollowing(){
        if (numFollowing<10)
            return "0"+numFollowing;
        return ""+numFollowing;
    }

    public void setAge(short age){this.age=age;}
    public void setNumPosts(short numPosts){this.numPosts=numPosts;}
    public void setNumFollowers(short numFollowers){this.numFollowers=numFollowers;}
    public void setNumFollowing(short numFollowing){this.numFollowing=numFollowing;}

}
