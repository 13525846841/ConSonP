package com.yksj.healthtalk.net.socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求参数设置类
 * SocketParams params = new SocketParams();
 * params.put("username", "james");
 * params.put("password", "123456");
 * </pre>
 */
public class SocketParams {
    private static String ENCODING = "UTF-8";

    protected ConcurrentHashMap<String, String> urlParams;
    protected JSONObject mJsonmap;
    

    /**
     * Constructs a new empty <code>RequestParams</code> instance.
     */
    public SocketParams() {
        init();
    }

    /**
     * Constructs a new RequestParams instance containing the key/value
     * string params from the specified map.
     * @param source the source key/value string map to add.
     */
    public SocketParams(Map<String, String> source) {
        init();

        for(Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     * @param key the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public SocketParams(String key, String value) {
        init();
        put(key, value);
    }

    /**
     * Adds a key/value string pair to the request.
     * @param key the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value){
        if(key != null && value != null) {
            urlParams.put(key, value);
            try {
				mJsonmap.put(key, value);
			} catch (JSONException e) {
			}
        }
    }

    /**
     * Removes a parameter from the request.
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key){
        urlParams.remove(key);
        mJsonmap.remove(key);
    }
    
    
    public JSONObject getParams(){
    	return mJsonmap;
    }

  

    private void init(){
        urlParams = new ConcurrentHashMap<String, String>();
        mJsonmap =new JSONObject();
    }


    public void putUtfString(String s, String s1) {
        put(s, s1);
    }

    public String getUtfString(String var1){
        return  mJsonmap.optString(var1);
    }

    public Integer getInt(String var1){
        return  mJsonmap.optInt(var1);
    }

    public void putInt(String var1, int var2){
        try {
            mJsonmap.put(var1,var2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean containsKey(String str){
        return mJsonmap.has(str);
    }


    public void putUtfStringArray(String str,Collection<String> codes){

    }
}
    