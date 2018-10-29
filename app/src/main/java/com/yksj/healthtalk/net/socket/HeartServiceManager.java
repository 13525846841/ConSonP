package com.yksj.healthtalk.net.socket;


/**
 * 心跳处理
 * @author jack_tang
 *
 */
public class HeartServiceManager extends IMManager {
	
	 /**单例模式*/
    private static HeartServiceManager inst = new HeartServiceManager();
    SocketManager mSocketManager = SocketManager.init();
    
    public static HeartServiceManager instance() {
        return inst;
    }

	@Override
	public void doOnStart() {
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}
	
	
	public void sendHeartMsg(){
		mSocketManager.sendSocketParams(null, SmartControlClient.SOCKET_HEART_CODE);
	}

}
