package com.yksj.healthtalk.net.http;
import org.json.JSONException;
import org.json.JSONTokener;

import android.os.Message;
import android.support.v4.app.FragmentActivity;

/**
 * 		asyncHttpClient.post(null,new SerializableHttpResponseHandler() {
			@Override
			public Object onParseResponse(String cotent) {
				return null;
			}
		});
 * http请求对调函数,将http返回的内容封装为自己想要的数据格式
 * @author zhao
 * 
 */
public abstract class ObjectHttpResponseHandler extends AsyncHttpResponseHandler {
	 
	public ObjectHttpResponseHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ObjectHttpResponseHandler(FragmentActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 成功返回
	 * @param response
	 */
	public void onSuccess(Object response) {
		
	}
	
	public void onSuccess(int statusCode, Object response) {
		onSuccess(response);
	}
	
	/**
	 * 必须实现此方法将string内容封装为自己定义的数据类型
	 * @param content
	 * @return
	 */
	public abstract Object onParseResponse(String content);

	/**
	 * 执行在后台线程
	 */
	@Override
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		try{
			Object jsonResponse = onParseResponse(responseBody);
			sendMessage(obtainMessage(PARSE_MESSAGE, new Object[] {statusCode, jsonResponse }));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 调用ui线程
	 */
	@Override
	protected void handleMessage(Message msg) {
		Object[] response;
        switch(msg.what) {
        	case PARSE_MESSAGE:
				response = (Object[]) msg.obj;
				handleSuccessSeriableMessage(((Integer) response[0]).intValue(),
						response[1]);
			break;
            case SUCCESS_MESSAGE:
                response = (Object[])msg.obj;
                handleSuccessMessage(((Integer) response[0]).intValue(), (String) response[1]);
                break;
            case FAILURE_MESSAGE:
                response = (Object[])msg.obj;
                handleFailureMessage((Throwable)response[0], (String)response[1]);
                break;
            case START_MESSAGE:
                onStart();
                break;
            case FINISH_MESSAGE:
                onFinish();
                break;
		}
	}

	protected void handleSuccessSeriableMessage(int statusCode, Object jsonResponse) {
		onSuccess(statusCode,jsonResponse);
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		onFailure(e, responseBody);
	}
	
	/**
	 * 将string转换为json,转换失败返回null
	 * @param responseBody
	 * @throws JSONException
	 */
    protected Object parseResponseToJson(String responseBody) throws JSONException {
        Object result = null;
		responseBody = responseBody.trim();
		if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
			result = new JSONTokener(responseBody).nextValue();
		}
		if (result == null) {
			result = responseBody;
		}
		return result;
    }

}
