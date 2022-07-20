//
// Created by MAY on 04/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_NOTIFICATIONMESSAGE_H
#define CONNECTIONHANDLER_CPP_NOTIFICATIONMESSAGE_H


#include <string>
#include "Message.h"

class NotificationMessage: public Message{
private:
    char pm_public;
    std::string postingUser;
    std::string content;
public:
    NotificationMessage(char _pm_public,  std::string _postingUser, std::string _content,  short _opCode);
    char getPm_public() const;
    std::string getPostingUser();
    std::string getContent();
    std::string toString();
};


#endif //CONNECTIONHANDLER_CPP_NOTIFICATIONMESSAGE_H
