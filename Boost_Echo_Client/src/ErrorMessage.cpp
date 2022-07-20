//
// Created by MAY on 04/01/2022.
//

#include "../include/ErrorMessage.h"

ErrorMessage::ErrorMessage(short _messageOpcode, short _opCode) : Message(_opCode), messageOpcode(_messageOpcode) {

}

short ErrorMessage::getMessageopcode() {
    return messageOpcode;
}

std::string ErrorMessage::toString() {
    std::string answer = "ERROR ";
    answer.append(std::to_string(getMessageopcode()));
    return answer;
}
