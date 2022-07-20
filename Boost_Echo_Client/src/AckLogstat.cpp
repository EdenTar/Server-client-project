//
// Created by MAY on 05/01/2022.
//

#include "../include/AckLogstat.h"

AckLogstat::AckLogstat(std::list<AckMessageUserInfo> _userInfos, short _messageOpcode, short _opCode) : AckMessage(_messageOpcode, _opCode) , userInfos(_userInfos){
}

std::string AckLogstat::toString() {
    std::string answer="ACK ";
    answer.append(std::to_string(getMessageOpCode()));
    answer.push_back(' ');
    for(AckMessageUserInfo messageUserInfo : userInfos){
        answer.push_back('\n');
        answer.append(messageUserInfo.toString());
    }

    return answer;
}
