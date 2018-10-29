package com.yksj.healthtalk.net.socket;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yksj.consultation.son.smallone.bean.Configs;

import org.json.JSONException;
import org.json.JSONObject;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.MaxConnectionsExceededException;
import org.xsocket.connection.NonBlockingConnection;
import org.xsocket.connection.NonBlockingConnectionPool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;

public class SocketManager {
	public static String IP = Configs.SOCKET_IP;
	private static int PORT = Configs.SOCKET_PORT;
	public static SocketManager mSocketManager;
	public static NonBlockingConnectionPool mPool;
	public static INonBlockingConnection mNbc;
	private static Gson mGson;
	public static final String SYSTEM_VERSION = "system_version";
	private final static int SOCKET_TAG = 10;// 临时加的 暂无用
	private boolean isConnection =false;
	private static final String osVersion = "";






	public static synchronized SocketManager init() {
		if (mSocketManager == null) {
			mSocketManager = new SocketManager();
			mGson = new Gson();
			mPool=new NonBlockingConnectionPool();
//			Resources appResources = HTalkApplication.getAppResources();
		}
		return mSocketManager;
	}
	public static XsocketHanlder xsocketHanlder;
	public static SocketManager getSocketManager(XsocketHanlder xsocketHanlder) {
		SocketManager.xsocketHanlder =xsocketHanlder;
		return init();
	}



	/**
	 * 总发送
	 *
	 * @param params
	 * @param SERVICE_CODE
	 */
	public static void sendSocketParams(SocketParams params, int SERVICE_CODE) {
		try {
			JSONObject jsonObject=new JSONObject();
			if(params!=null)
				jsonObject.put("server_params", params.getParams());
			jsonObject.put("server_code", SERVICE_CODE);
			jsonObject.put("tag", SOCKET_TAG);
			jsonObject.put(SYSTEM_VERSION, osVersion);
			if (SERVICE_CODE == 100) {
			} else {
				Logger.json(jsonObject.toString());
			}
			write(jsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 最终写法
	 *
	 * @param params
	 */
	private static void write(String params) {
		try {
			mNbc.write(params);
			mNbc.write(SmartControlClient.BYDELIMITER);
			mNbc.flush();
		} catch (BufferOverflowException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接
	 */
	public synchronized void connect() {
		try {
			if (mNbc != null)
				mNbc.close();
			   mNbc = new NonBlockingConnection(InetAddress.getByName(IP),
					PORT, xsocketHanlder, true, SmartControlClient.LOGIN_TIMEOUT);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (MaxConnectionsExceededException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开
	 */
	public synchronized void disConnect() {
		if (mNbc != null && mNbc.isOpen())
			try {
//				mPool.close();
				mNbc.close();
			} catch (IOException e) {

			}
	}


	public boolean isConnected(){
		if(mNbc !=null && mNbc.isOpen())
			return true;

		return false;
	}
}
