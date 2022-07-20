//
// Created by ynal6 on 05/01/2022.
//

#include "../include/ObjectEncoderDecoder.h"
#include "../include/AckMessage.h"
#include "../include/NotificationMessage.h"
#include "../include/AckFollow_UnfollowMessage.h"
#include "../include/AckLogstat.h"
#include "../include/AckStatMessage.h"
#include "../include/ErrorMessage.h"

#include <boost/lexical_cast.hpp>
#include <iostream>

ObjectEncoderDecoder::ObjectEncoderDecoder()
{

}

Message* ObjectEncoderDecoder::decode(std::string answer) {
    short opCode = boost::lexical_cast<short>(answer.substr(0, 2));
    answer.erase(0,2);
    answer.pop_back();
    if (opCode == 9)
    {
        return decodeNotification(answer,opCode);
    }
    else if (opCode==10) {
        return decodeAck(answer,opCode);
    }
    else if (opCode == 11)
    {
        return decodeError(answer,opCode);
    }
    return (new Message((short)0));
}


Message* ObjectEncoderDecoder::decodeNotification(std::string answer, short opCode) {
    char pm_post = (answer.substr(0,1))[0];
    answer.erase(0,1);
// output as before
    std::stringstream test(answer);
    std::string segment;
    std::vector<std::string> seglist;

    while(std::getline(test, segment, '\0'))
    {
        seglist.push_back(segment);
    }
    std::string postingUser, content;
    postingUser = seglist[0];
    content = seglist[1];

//    char pm_post = answer[0];
//    answer.erase(0,1);
//    std::string postingUser, content;
//    std::string delimiter = "\0";
//    size_t pos = 0;
//    pos = answer.find(delimiter);
//    postingUser = answer.substr(0, pos);
//    answer.erase(0, pos + delimiter.length());
//    pos = answer.find(delimiter);
//    content = answer.substr(0, pos);
//    answer.erase(0, pos + delimiter.length());
    NotificationMessage *notificationMessage = new NotificationMessage(pm_post,  postingUser, content, opCode);
    return notificationMessage;
}

Message* ObjectEncoderDecoder::decodeAckStatLogstat(std::string answer, short opCode, short messageOpcode) {
    //create userInfos
    std::list<AckMessageUserInfo> userInfos = {};
    std::string delimiter = "\\|";
    size_t pos = 0;
    std::string token;
    while ((pos = answer.find(delimiter)) != std::string::npos) {
        token = answer.substr(0, pos);
        //age
        std::string ageS = token.substr(0, 2);
        short age = boost::lexical_cast<short>(ageS);
        token.erase(0,2);
        //num posts
        std::string numPostsS = token.substr(0, 2);
        short numPosts = boost::lexical_cast<short>(numPostsS);
        token.erase(0,2);
        //num followers
        std::string numFollowersS = token.substr(0, 2);
        short numFollowers = boost::lexical_cast<short>(numFollowersS);
        token.erase(0,2);
        //num following
        std::string numFollowingS = token.substr(0, 2);
        short numFollowing = boost::lexical_cast<short>(numFollowingS);
        token.erase(0,2);

        userInfos.push_back(*(new AckMessageUserInfo(age,numPosts,numFollowers,numFollowing)));
        answer.erase(0, pos + delimiter.length());
    }

    if(messageOpcode==7){
        AckLogstat *ackLogstat = new AckLogstat(userInfos,messageOpcode,opCode);
        return ackLogstat;
    }
    else {
        AckStatMessage *ackStatMessage = new AckStatMessage(userInfos, messageOpcode, opCode);
        return ackStatMessage;
    }
}

Message* ObjectEncoderDecoder::decodeAck(std::string answer, short opCode) {
    std::string s = answer.substr(0, 1);
    short messageOpcode = boost::lexical_cast<short>(s);
    answer.erase(0,1);

    if (messageOpcode == 1 || messageOpcode == 2 || messageOpcode == 3 || messageOpcode == 5 ||
        messageOpcode == 6) {
        if (answer=="2")
        {
            s += answer.substr(0, 1);
            messageOpcode = boost::lexical_cast<short>(s);
            answer.erase(0,1);
        }
        AckMessage *ackMessage = new AckMessage(messageOpcode, opCode);
        return ackMessage;
    } else if (messageOpcode == 4)
    {
        std::string username = answer;
        username.pop_back();
        AckFollow_UnfollowMessage *ackFollowUnfollowMessage = new AckFollow_UnfollowMessage(username, messageOpcode, opCode);
        return ackFollowUnfollowMessage;
    } else if (messageOpcode == 7 || messageOpcode == 8) {
        return decodeAckStatLogstat(answer,opCode,messageOpcode);
    }
    return (new Message((short)0));
}

Message* ObjectEncoderDecoder::decodeError(std::string answer, short opCode) {
    std::string s = answer.substr(0, 2);
    short messageOpcode = boost::lexical_cast<short>(s);
    return (new ErrorMessage(messageOpcode,opCode));
}

/*std::string ObjectEncoderDecoder::encode(Message* message) {
   short opCode=message->getOpcode();
    std::string output=std::to_string(opCode);
    std::cout<<"starting  encode";
    if(opCode==1){
        std::cout<<"starting register encode";
        output+=((RegisterMessage*)&message)->getUsername()+"0";
        std::cout<<"after line 1 register encode";
        output+=((RegisterMessage*)&message)->getPassword()+"0";
        output+=((RegisterMessage*)&message)->getBirthday();
   }
   else if(opCode==2){
        output+=((LoginMessage*)&message)->getUsername()+"0";
        output+=((LoginMessage*)&message)->getPassword()+"0";
        output+=((LoginMessage*)&message)->getCaptcha();
   }

   else if(opCode==4){
       output+=((Follow_UnfollowMessage*)&message)->getFollow();
       output+=((Follow_UnfollowMessage*)&message)->getUsername()+"0";
   }
   else if(opCode==5){
        output+=((PostMessage*)&message)->getContent()+"0";
   }
   else if(opCode==6){
        output+=((PMMessage*)&message)->getUsername()+"0";
        output+=((PMMessage*)&message)->getContent()+"0";
        output+=((PMMessage*)&message)->getSendingDateTime()+"0";
    }

   else if(opCode==8){
       std::string usernames="";
       for(std::string user : ((StatsMessage*)&message)->getUsers() ){
           usernames+=user+"|";
       }
       output+=usernames+"0";
   }
   else if(opCode==12){
        output+=((PMMessage*)&message)->getUsername()+"0";
   }
    delete message;
    return output;

}*/



