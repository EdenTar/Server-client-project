//
// Created by MAY on 05/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_ACKLOGSTAT_H
#define CONNECTIONHANDLER_CPP_ACKLOGSTAT_H


#include <list>
#include <string>
#include "AckMessage.h"
#include "AckMessageUserInfo.h"

class AckLogstat : public AckMessage{
private:
    std::list<AckMessageUserInfo> userInfos;
public:
    AckLogstat(std::list<AckMessageUserInfo> _userInfos,short _messageOpcode, short _opCode);
    std::string toString();
};


#endif //CONNECTIONHANDLER_CPP_ACKLOGSTAT_H
