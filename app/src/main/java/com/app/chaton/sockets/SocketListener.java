package com.app.chaton.sockets;

import com.app.chaton.API_helpers.Message;

public interface SocketListener {

    Long getCompanionId();

    void setTyping(boolean isTyping);

    void onMessageReceived(Message message);

    void onTypePending();
    void stopTyping();
}
