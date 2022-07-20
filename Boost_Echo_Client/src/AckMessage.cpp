//
// Created by MAY on 04/01/2022.
//

#include "../include/AckMessage.h"

AckMessage::AckMessage(short _messageOpcode, short _opCode) : Message(_opCode), messageOpcode(_messageOpcode) {
}

short AckMessage::getMessageOpCode() {
    return messageOpcode;
}

std::string AckMessage::toString() {
    std::string answer = "ACK ";
    answer.append(std::to_string(getMessageOpCode()));
    return answer;
}
