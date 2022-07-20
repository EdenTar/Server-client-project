//
// Created by MAY on 05/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_ACKSTATMESSAGE_H
#define CONNECTIONHANDLER_CPP_ACKSTATMESSAGE_H


#include <list>
#include "AckMessageUserInfo.h"
#include "AckMessage.h"

class AckStatMessage : public AckMessage{
private:
    std::list<AckMessageUserInfo> userInfos;
public:
    AckStatMessage(std::list<AckMessageUserInfo> _userInfos,short _messageOpcode, short _opCode);
    std::string toString();
};


#endif //CONNECTIONHANDLER_CPP_ACKSTATMESSAGE_H
