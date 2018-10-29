package com.yksj.healthtalk.net.http;

import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * jf json
 * @author zhao
 * 
 */
public class JsonsfHttpResponseHandler extends AsyncHttpResponseHandler {
	
	protected static final int SUCCESS_JSON_MESSAGE = 100;
	
	//
	// Callbacks to be overridden, typically anonymously
	//

	public JsonsfHttpResponseHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JsonsfHttpResponseHandler(FragmentActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Fired when a request returns successfully and contains a json object at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param response
	 *            the parsed json object found in the server response (if any)
	 */
	public void onSuccess(JSONObject response) {
	}

	/**
	 * Fired when a request returns successfully and contains a json array at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param response
	 *            the parsed json array found in the server response (if any)
	 */
	public void onSuccess(JSONArray response) {
	}

	/**
	 * Fired when a request returns successfully and contains a json object at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param response
	 *            the parsed json object found in the server response (if any)
	 */
	public void onSuccess(int statusCode, JSONObject response) {
		onSuccess(response);
	}

	/**
	 * Fired when a request returns successfully and contains a json array at
	 * the base of the response string. Override to handle in your own code.
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param response
	 *            the parsed json array found in the server response (if any)
	 */
	public void onSuccess(int statusCode, JSONArray response) {
		onSuccess(response);
	}

	public void onFailure(Throwable e, JSONObject errorResponse) {
	}

	public void onFailure(Throwable e, JSONArray errorResponse) {
	}

	//
	// Pre-processing of messages (executes in background threadpool thread)
	//

	@Override
	protected void sendSuccessMessage(int statusCode, String responseBody) {
		try {
			Object jsonResponse = parseResponse(responseBody);
			sendMessage(obtainMessage(SUCCESS_JSON_MESSAGE, new Object[] {
					statusCode, jsonResponse }));
		} catch (JSONException e) {
			sendFailureMessage(e, responseBody);
		}
	}

	//
	// Pre-processing of messages (in original calling thread, typically the UI
	// thread)
	//

	@Override
	protected void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS_JSON_MESSAGE:
			Object[] response = (Object[]) msg.obj;
			handleSuccessJsonMessage(((Integer) response[0]).intValue(),
					response[1]);
			break;
		default:
			super.handleMessage(msg);
		}
	}

	protected void handleSuccessJsonMessage(int statusCode, Object jsonResponse) {
		if (jsonResponse instanceof JSONObject) {
			onSuccess(statusCode, (JSONObject) jsonResponse);
		} else if (jsonResponse instanceof JSONArray) {
			onSuccess(statusCode, (JSONArray) jsonResponse);
		} else {
			onFailure(new JSONException("Unexpected type "
					+ jsonResponse.getClass().getName()), (JSONObject) null);
		}
	}

	protected Object parseResponse(String responseBody) throws JSONException {
		Object result = null;
		// trim the string to prevent start with blank, and test if the string
		// is valid JSON, because the parser don't do this :(. If Json is not
		// valid this will return null
		responseBody = responseBody.trim();
		if (responseBody.startsWith("{") || responseBody.startsWith("[")) {
			try{
				result = JSON.parse(responseBody);
			}catch(JSONException e){
			}
			//result = new JSONTokener(responseBody).nextValue();
		}
		if (result == null) {
			result = responseBody;
		}
		return result;
	}

	@Override
	protected void handleFailureMessage(Throwable e, String responseBody) {
		try {
			if (responseBody != null) {
				Object jsonResponse = parseResponse(responseBody);
				if (jsonResponse instanceof JSONObject) {
					onFailure(e, (JSONObject) jsonResponse);
				} else if (jsonResponse instanceof JSONArray) {
					onFailure(e, (JSONArray) jsonResponse);
				} else {
					onFailure(e, responseBody);
				}
			} else {
				onFailure(e, "");
			}
		} catch (JSONException ex) {
			onFailure(e, responseBody);
		}
	}
}
