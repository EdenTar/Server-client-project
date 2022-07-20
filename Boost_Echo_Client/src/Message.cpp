//
// Created by MAY on 04/01/2022.
//

#include "../include/Message.h"

Message::Message(short _opCode) : opCode(_opCode) {
}

short Message::getOpcode() {
    return opCode;
}

std::string Message::toString() {
    return std::string();
}


