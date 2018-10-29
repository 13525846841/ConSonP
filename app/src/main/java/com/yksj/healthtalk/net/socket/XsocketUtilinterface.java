package com.yksj.healthtalk.net.socket;

import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IConnectionTimeoutHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;

public interface XsocketUtilinterface extends IDataHandler, IConnectHandler, IDisconnectHandler,IConnectionTimeoutHandler {

}
