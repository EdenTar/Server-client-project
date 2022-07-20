//
// Created by MAY on 05/01/2022.
//

#include <iostream>
#include "../include/AckFollow_UnfollowMessage.h"

AckFollow_UnfollowMessage::AckFollow_UnfollowMessage(std::string _username, short _messageOpcode, short _opCode): AckMessage(_messageOpcode, _opCode), username(_username) {

}

std::string AckFollow_UnfollowMessage::toString() {
    std::string answer = "ACK ";
    answer.append(std::to_string(getMessageOpCode()));
    answer.append(" ");
    answer.append(username);
    return answer;
}
