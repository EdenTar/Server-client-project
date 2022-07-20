//
// Created by MAY on 05/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_ACKMESSAGEUSERINFO_H
#define CONNECTIONHANDLER_CPP_ACKMESSAGEUSERINFO_H


#include <string>

class AckMessageUserInfo {
private:
    short age;
    short numPosts;
    short numFollowers;
    short numFollowing;
public:
    AckMessageUserInfo(short _age, short _numPosts, short _numFollowers, short _numFollowing);
    short getNumPosts();
    short getAge();
    short getNumFollowers();
    short getNumFollowing();
    std::string toString();
};


#endif //CONNECTIONHANDLER_CPP_ACKMESSAGEUSERINFO_H
