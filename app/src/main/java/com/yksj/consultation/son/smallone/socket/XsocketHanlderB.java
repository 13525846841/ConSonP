package com.yksj.consultation.son.smallone.socket;

import com.yksj.healthtalk.net.socket.XsocketUtilinterface;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

public class XsocketHanlderB implements XsocketUtilinterface {

	private final String TAG = "XSocket";
	private static XsocketHanlderB xsocketHanlderB = new XsocketHanlderB();

	public static XsocketHanlderB getInstance(XsocketHanlderListeren listeren) {
		if(xsocketHanlderB ==null){
			xsocketHanlderB = new XsocketHanlderB();
			//KLog.d("==XsocketHanlderB===重新创建了");
		}
		SocketManagerB.getSocketManager(xsocketHanlderB);
		xsocketHanlderB.setXsocketHanlderListeren(listeren);
		return xsocketHanlderB;
	}

	public interface XsocketHanlderListeren{
		void XsocketonConnect(INonBlockingConnection arg0);
		void XsocketonDisconnect(INonBlockingConnection arg0);
		void XsocketonData(INonBlockingConnection arg0);
	}

	public XsocketHanlderListeren listeren;
	public void setXsocketHanlderListeren(XsocketHanlderListeren listeren){
		this.listeren=listeren;
	}



	public boolean onDisconnect(INonBlockingConnection arg0) throws IOException {
		if(listeren!=null)
			listeren.XsocketonDisconnect(arg0);
		//System.out.println("-------------onDisconnect---");
		//Log.d(TAG, "---------onDisconnect");
		return true;
	}

	public boolean onConnect(INonBlockingConnection arg0) throws IOException,
			BufferUnderflowException, MaxReadSizeExceededException {
		if(listeren!=null)
			listeren.XsocketonConnect(arg0);

		return true;
	}

	public boolean onData(INonBlockingConnection arg0) throws IOException,
			BufferUnderflowException, ClosedChannelException,
			MaxReadSizeExceededException {
		if(listeren!=null)
			listeren.XsocketonData(arg0);

		return true;
	}

	@Override
	public boolean onConnectionTimeout(INonBlockingConnection arg0)
			throws IOException {
		System.out.println("-------------onConnectionTimeout---");
		return false;
	}

}