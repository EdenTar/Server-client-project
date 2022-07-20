//
// Created by MAY on 05/01/2022.
//

#include "../include/AckStatMessage.h"
/*REGISTER E 1 11-11-1111
LOGIN E 1 1
REGISTER S 1 11-11-1111
REGISTER D 1 11-11-1111
REGISTER A 1 01-12-2002
STAT A|D|S

REGISTER E 1 11-11-1111
LOGIN E 1 1
REGISTER S 1 11-11-1111
REGISTER D 1 11-11-1111
REGISTER A 1 01-12-2002
STAT A|D|S
19 0 0 0
91 0 0 0
BLOCK S
ACK 8
STAT A|D|S
 */

AckStatMessage::AckStatMessage(std::list<AckMessageUserInfo> _userInfos, short _messageOpcode, short _opCode) : AckMessage(_messageOpcode, _opCode), userInfos(_userInfos) {
}

std::string AckStatMessage::toString() {
    std::string answer="ACK ";
    answer.append(std::to_string(getMessageOpCode()));
    answer.push_back(' ');
    for(AckMessageUserInfo messageUserInfo : userInfos){
        answer.push_back('\n');
        answer.append(messageUserInfo.toString());
    }
    return answer;
}
