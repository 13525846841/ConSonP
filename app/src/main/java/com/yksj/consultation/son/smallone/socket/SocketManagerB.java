package com.yksj.consultation.son.smallone.socket;

import com.google.gson.Gson;
import com.yksj.consultation.comm.SOConfigs;

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

public class SocketManagerB {
	private static final String TAG = "SocketManagerB";

	private static String IP = SOConfigs.SOCKET_IP;
	private static int PORT = SOConfigs.SOCKET_PORT;
	public static SocketManagerB mSocketManagerB;
	public static NonBlockingConnectionPool mPool;
	public static INonBlockingConnection mNbc;
	private static Gson mGson;
	public static final String SYSTEM_VERSION = "system_version";
	private final static int SOCKET_TAG = 10;// 临时加的 暂无用
	private boolean isConnection =false;
	private static String osVersion = "";

	public static synchronized SocketManagerB init() {
		if (mSocketManagerB == null) {
			mSocketManagerB = new SocketManagerB();
			mGson = new Gson();
			mPool=new NonBlockingConnectionPool();
			osVersion = SOConfigs.VERSION_SYS;
		}
		return mSocketManagerB;
	}

	public static XsocketHanlderB xsocketHanlderB;
	public static SocketManagerB getSocketManager(XsocketHanlderB xsocketHanlderB) {
//		IP = ip;
//		PORT =Integer.valueOf(port);
		SocketManagerB.xsocketHanlderB = xsocketHanlderB;
		return init();
	}

	/**
	 * 总发送
	 * @param SERVICE_CODE
	 */
	public static void sendSocketParams(JSONObject jsonObject, int SERVICE_CODE) {
		try {
			if(jsonObject!=null){
				jsonObject.put("server_code", SERVICE_CODE);
				jsonObject.put("tag", SOCKET_TAG);
				jsonObject.put(SYSTEM_VERSION, osVersion);
				//KLog.d("发送===CODE=="+SERVICE_CODE);
				write(jsonObject.toString());
			}
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
			//KLog.d("发送==="+params);
			mNbc.write(params);
			mNbc.write(SocketCode.BYDELIMITER);
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

			if(xsocketHanlderB ==null){
				//KLog.d("Socket监听为null");
			}

			if (mNbc != null)
				mNbc.close();
			mNbc = new NonBlockingConnection(InetAddress.getByName(IP),
					PORT, xsocketHanlderB, true, SocketCode.LOGIN_TIMEOUT);
		} catch (SocketTimeoutException e) {
			//LogUtil.d(TAG, "SocketTimeoutException 链接超时");
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
		if (isConnected())
			try {
				//KLog.d(TAG, "=====dispatch====断开");
				mNbc.close();
			} catch (IOException e) {

			}
	}
	
	
	public static boolean isConnected(){
		if(mNbc !=null && mNbc.isOpen())
			return true;
		return false;
	}
}
