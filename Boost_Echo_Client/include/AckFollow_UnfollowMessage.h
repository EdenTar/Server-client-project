//
// Created by MAY on 05/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_ACKFOLLOW_UNFOLLOWMESSAGE_H
#define CONNECTIONHANDLER_CPP_ACKFOLLOW_UNFOLLOWMESSAGE_H


#include <string>
#include "AckMessage.h"

class AckFollow_UnfollowMessage : public AckMessage{
private:
    std::string username;
public:
    AckFollow_UnfollowMessage(std::string _username,short _messageOpcode, short _opCode);
    std::string toString();
};


#endif //CONNECTIONHANDLER_CPP_ACKFOLLOW_UNFOLLOWMESSAGE_H
