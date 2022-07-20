cmake_minimum_required(VERSION 3.20)
project(Boost_Echo_Client)

set(CMAKE_CXX_STANDARD 11)



set(Boost_INCLUDE_DIR /cygdrive/c/cygwin64/usr/include)
find_package(Boost REQUIRED COMPONENTS system)
include_directories(${Boost_INCLUDE_DIRS})
add_executable(Boost_Echo_Client src/connectionHandler.cpp include/connectionHandler.h src/echoClient.cpp src/keyboardInput.cpp include/keyboardInput.h src/serverInput.cpp include/serverInput.h src/clientBGSProtocol.cpp src/messages/RegisterMessage.cpp include/messages/RegisterMessage.h src/messages/LoginMessage.cpp include/messages/LoginMessage.h src/messages/LogoutMessage.cpp include/messages/LogoutMessage.h src/messages/Message.cpp include/messages/Message.h src/messages/Follow_UnfollowMessage.cpp include/messages/Follow_UnfollowMessage.h src/messages/PostMessage.cpp include/messages/PostMessage.h src/messages/PMMessage.cpp include/messages/PMMessage.h src/messages/NotificationMessage.cpp include/messages/NotificationMessage.h src/messages/ErrorMessage.cpp include/messages/ErrorMessage.h src/messages/AckMessage.cpp include/messages/AckMessage.h src/messages/BlockMessage.cpp include/messages/BlockMessage.h src/messages/LogstatMessage.cpp include/messages/LogstatMessage.h src/messages/StatsMessage.cpp include/messages/StatsMessage.h)
target_link_libraries(${PROJECT_NAME} ${Boost_LIBRARIES})
