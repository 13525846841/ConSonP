package com.yksj.healthtalk.net.socket;

import android.util.Log;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

public class XsocketHanlder implements XsocketUtilinterface {

	private final String TAG = "XSocket";
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
		System.out.println("-------------onDisconnect---");
		Log.d(TAG, "---------onDisconnect");
		return true;
	}

	public boolean onConnect(INonBlockingConnection arg0) throws IOException,
			BufferUnderflowException, MaxReadSizeExceededException {
		if(listeren!=null)
			listeren.XsocketonConnect(arg0);

//		if (LoginServiceManeger.instance().loginState == LoginEvent.LOGINING) {
//			LoginServiceManeger.instance().login();
//		}
		System.out.println("-------------onConnect---");
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