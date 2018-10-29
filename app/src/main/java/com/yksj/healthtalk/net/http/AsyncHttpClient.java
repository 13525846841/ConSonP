/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.yksj.healthtalk.net.http;

import android.content.Context;

import com.yksj.consultation.son.app.AppData;
import com.yksj.consultation.son.app.HTalkApplication;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * 异步http请求
 * The AsyncHttpClient can be used to make asynchronous GET, POST, PUT and
 * DELETE HTTP requests in your Android applications. Requests can be made
 * with additional parameters by passing a {@link RequestParams} instance,
 * and responses can be handled by passing an anonymously overridden
 * {@link AsyncHttpResponseHandler} instance.
 * <p>
 * For example:
 * <p>
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get("http://www.google.com", new AsyncHttpResponseHandler() {
 *     &#064;Override
 *     public void onSuccess(String response) {
 *     }
 * });
 * </pre>
 */
public class AsyncHttpClient {
    private static final String VERSION = "1.4.1";
//    private static final String SYSTEM_VERSION = "3.1.0";

    private static final int DEFAULT_MAX_CONNECTIONS = 20;//最大连接时间
    private static final int DEFAULT_SOCKET_TIMEOUT = 20 * 1000;//timeout时间
    private static final int DEFAULT_MAX_RETRIES = 0;//重连的最大次数
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";
    public static final String FORM_CONTENT_TYPE = "multipart/form-data";//表单类型

    private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;
    private ThreadPoolExecutor threadPool;


    private final Map<String, String> clientHeaderMap;
    private final Map<Context, List<WeakReference<Future<?>>>> requestMap;//异步回调引用
    private String CER_61_TEST =
            "-----BEGIN CERTIFICATE-----\n" +
                    "MIIDXDCCAkQCCQCmgd8JH66dYzANBgkqhkiG9w0BAQsFADBvMQswCQYDVQQGEwJD\n" +
                    "TjEQMA4GA1UECAwHQmVpamluZzEQMA4GA1UEBwwHQmVpamluZzEQMA4GA1UECgwH\n" +
                    "SVQgdGVzdDEQMA4GA1UECwwHSVQgRGVwdDEYMBYGA1UEAwwPNjEuamlhbnNpb24u\n" +
                    "Y29tMCAXDTE4MDIyNzA2MDA1M1oYDzIxMTgwMjAzMDYwMDUzWjBvMQswCQYDVQQG\n" +
                    "EwJDTjEQMA4GA1UECAwHQmVpamluZzEQMA4GA1UEBwwHQmVpamluZzEQMA4GA1UE\n" +
                    "CgwHSVQgdGVzdDEQMA4GA1UECwwHSVQgRGVwdDEYMBYGA1UEAwwPNjEuamlhbnNp\n" +
                    "b24uY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzgzNAMvWSzM+\n" +
                    "iqvuef5wGXv9Nz2BZl8swRJq/KHH+PB55lpg6LQmKF98XsIM+YFjuEod/2VG+QLu\n" +
                    "8Qgbt/1PRfiAKy2ErBHVuOFHq9sbcT/BCKxFasUfTDPbfOuGBxrEjTrBTlXu1rwA\n" +
                    "UUkgxyAVVIAB3k8B20r7F2Mk/HvxX3xxDLbC9MMNr1Kp/OKe/hVZfTyF2TbaLCJ6\n" +
                    "+kbBZjT80im27Aa3JCWSsF808K95KDoxV95Q0GfOzty7U/kCwJA4BnoylnIMOvN4\n" +
                    "krCPWjtivJUUbL6ADup3GeCYUhr61/gKrsqLgltHPe982jSapDp4wx7099x7W2N1\n" +
                    "R8B8SX2AjwIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQBPjCESOwYSAjwhRe3QLqLT\n" +
                    "rSHCywmHsoeFOnYI8ZlQDM/hR4AmjZlwRMPV2SKoxkBAUQ6zCIsdBvAjPqfzNxGg\n" +
                    "FSmjN7kluBhLGp8/tTAQNxIkj3/ueKFVSjkTkmApwV28XINsGlXBQ1kuiUMDLp1Y\n" +
                    "3IqGhC4Dvsxj1NChalMvzBN5dfFdYkukKBt/BVLr0RqOyEYrKZ5SrZom2d5jildP\n" +
                    "YShTQRPwDbV8Qf0QAAJfVRHcEVOw5OspXv3tWMb4DKEZoNTpTLzNOMmaznSt1Rwe\n" +
                    "NK1LDgrSIfI0iTT4Q08MOUdvnSHcb/SUuvxDXE6yvBsxlZQ5YmAR9Dfl4U3OIY3j\n" +
                    "-----END CERTIFICATE-----\n";//六一测试服务器认证证书

    /**
     * Creates a new AsyncHttpClient.
     */
    public AsyncHttpClient(boolean isHttps) {
        BasicHttpParams httpParams = new BasicHttpParams();
//      ConnManagerParams.setTimeout(httpParams, socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
        ConnManagerParams.setTimeout(httpParams, 60000 * 3); //设置连接最大等待时间

        HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);//读取数据
        HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);//连接超时
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
        HttpProtocolParams.setContentCharset(httpParams, "UTF-8");

        HttpProtocolParams.setUseExpectContinue(httpParams, false);//
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(httpParams, System.getProperty("http.agent"));


//        if (isHttps){
//            // 支持http与https
//            KeyStore trustStore = null;
//            try {
//                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                trustStore.load(HTalkApplication.getApplication().getAssets().open("61jiansion.keystore"), "111111".toCharArray());
//                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
//                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//                SchemeRegistry schemeRegistry = new SchemeRegistry();
//                schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
////            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//                schemeRegistry.register(new Scheme("https", sf, 443));
//                // ThreadSafeClientConnManager线程安全管理类
////                ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
////                httpClient = new DefaultHttpClient(cm, httpParams);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//      HttpHost host = new HttpHost(hostname)
//      HttpProtocolParams.setUserAgent(httpParams, String.format("android-async-http/%s (http://loopj.com/android-async-http)", VERSION));
//      HttpClientParams.setRedirecting(httpParams, true);
        //是否允许重定向到相同路径
        httpParams.setBooleanParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        schemeRegistry.register(new Scheme("https", createAdditionalCertsSSLSocketFactory(), 443));
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

        httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        httpClient = new DefaultHttpClient(cm, httpParams);
       /* httpClient.setRedirectHandler(new RedirectHandler() {
            @Override
			public boolean isRedirectRequested(HttpResponse response,
					HttpContext context) {
				return true;
			}
			@Override
			public URI getLocationURI(HttpResponse response, HttpContext context)
					throws ProtocolException {
				if(response.containsHeader("Location")){
					return URI.create(response.getFirstHeader("Location").getValue());
				}else{
					return null;
				}
			}
		});*/

        //请求拦截器,在发送数据之前调用
        httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(HttpRequest request, HttpContext context) {
                //进行zip压缩方式发送
                if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
                //添加http头
                for (String header : clientHeaderMap.keySet()) {
                    request.addHeader(header, clientHeaderMap.get(header));
                }
            }
        });

        //在响应之前进行拦截
        httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(HttpResponse response, HttpContext context) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();//获得编码方式
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        //如果服务器是zip方式发送则进行数据流的转换
                        if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            break;
                        }
                    }
                }
            }
        });


        //设置请求重连机制
        httpClient.setHttpRequestRetryHandler(new RetryHandler(DEFAULT_MAX_RETRIES));
        threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();//创建一个可根据需要创建新线程的线程池
        requestMap = new WeakHashMap<Context, List<WeakReference<Future<?>>>>();//事件回调函数
        clientHeaderMap = new HashMap<String, String>();
        setDefualtHeader();
    }

    protected org.apache.http.conn.ssl.SSLSocketFactory createAdditionalCertsSSLSocketFactory() {
        try {
            final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            // the bks file we generated above
            final InputStream in = HTalkApplication.getApplication().getAssets().open("my.keystore");
            try {
                // don't forget to put the password used above in strings.xml/mystore_password
                ks.load(in, AppData.CERT_PASS.toCharArray());
            } finally {
                in.close();
            }

            return new AdditionalKeyStoresSSLSocketFactory(ks);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * [dictionary setObject:[NSString systemVersion] forKey:@"system_version"];
     [dictionary setObject:username forKey:@"username"];
     [dictionary setObject:password forKey:@"password"];
     [dictionary setObject:@"0" forKey:@"os_type"]
     */
    /**
     * 设置默认的HTTP header
     */
    private void setDefualtHeader() {
        addHeader("system_version", HTalkApplication.getAppVersionName());
        addHeader("client_type", HTalkApplication.CLIENT_TYPE);
        addHeader("OSType", "1");
    }

    public Map<String, String> getHeaders() {
        return clientHeaderMap;
    }

    /**
     * Sets headers that will be added to all requests this client makes (before sending).
     *
     * @param header the name of the header
     * @param value  the contents of the header
     */
    public void addHeader(String header, String value) {
        clientHeaderMap.put(header, value);
    }

    /**
     * Sets an optional CookieStore to use when making requests
     *
     * @param cookieStore The CookieStore implementation to use, usually an instance of {@link PersistentCookieStore}
     */
    public void setCookieStore(CookieStore cookieStore) {
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }


    /**
     * Overrides the threadpool implementation used when queuing/pooling
     * requests. By default, Executors.newCachedThreadPool() is used.
     *
     * @param threadPool an instance of {@link ThreadPoolExecutor} to use for queuing/pooling requests.
     */
    public void setThreadPool(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * Sets the User-Agent header to be sent with each request. By default,
     * "Android Asynchronous Http Client/VERSION (http://loopj.com/android-async-http/)" is used.
     *
     * @param userAgent the string to use in the User-Agent header.
     */
    public void setUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
    }

    /**
     * Sets the connection time oout. By default, 10 seconds
     *
     * @param timeout the connect/socket timeout in milliseconds
     */
    public void setTimeout(int timeout) {
        final HttpParams httpParams = this.httpClient.getParams();
        ConnManagerParams.setTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
    }

    /**
     * Sets the SSLSocketFactory to user when making requests. By default,
     * a new, default SSLSocketFactory is used.
     *
     * @param sslSocketFactory the socket factory to use for https requests.
     */
    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sslSocketFactory, 443));
    }

    /**
     * Sets basic authentication for the request. Uses AuthScope.ANY. This is the same as
     * setBasicAuth('username','password',AuthScope.ANY)
     */
    public void setBasicAuth(String user, String pass) {
        AuthScope scope = AuthScope.ANY;
        setBasicAuth(user, pass, scope);
    }

    /**
     * Sets basic authentication for the request. You should pass in your AuthScope for security. It should be like this
     * setBasicAuth("username","password", new AuthScope("host",port,AuthScope.ANY_REALM))
     *
     * @param scope - an AuthScope object
     */
    public void setBasicAuth(String user, String pass, AuthScope scope) {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, pass);
        this.httpClient.getCredentialsProvider().setCredentials(scope, credentials);
    }

    /**
     * Cancels any pending (or potentially active) requests associated with the
     * passed Context.
     * <p>
     * <b>Note:</b> This will only affect requests which were created with a non-null
     * android Context. This method is intended to be used in the onDestroy
     * method of your android activities to destroy all requests which are no
     * longer required.
     *
     * @param context               the android Context instance associated to the request.
     * @param mayInterruptIfRunning specifies if active requests should be cancelled along with pending requests.
     */
    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
        List<WeakReference<Future<?>>> requestList = requestMap.get(context);
        if (requestList != null) {
            for (WeakReference<Future<?>> requestRef : requestList) {
                Future<?> request = requestRef.get();
                if (request != null) {
                    request.cancel(mayInterruptIfRunning);
                }
            }
        }
        requestMap.remove(context);
    }

    //
    // HTTP GET Requests
    //

    /**
     * Perform a HTTP GET request, without any parameters.
     *
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        get(null, url, null, responseHandler);
    }

    /**
     * Perform a HTTP GET request with parameters.
     *
     * @param url             the URL to send the request to.
     * @param params          additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        get(null, url, params, responseHandler);
    }

    /*
     * Perform a HTTP GET request without any parameters and track the Android Context which initiated the request.
     * @param context the Android Context which initiated the request.
     * @param url the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        get(context, url, null, responseHandler);
    }

    /**
     * Perform a HTTP GET request and track the Android Context which initiated the request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param params          additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        sendRequest(httpClient, httpContext, new HttpGet(getUrlWithQueryString(url, params)), null, responseHandler, context);
    }

    /**
     * Perform a HTTP GET request and track the Android Context which initiated
     * the request with customized headers
     *
     * @param url             the URL to send the request to.
     * @param headers         set headers only for this request
     * @param params          additional GET parameters to send with the request.
     * @param responseHandler the response handler instance that should handle
     *                        the response.
     */
    public void get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null) request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, null, responseHandler,
                context);
    }

    //
    // HTTP POST Requests
    //

    /**
     * 执行一个不带参数的http post请求
     *
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(String url, AsyncHttpResponseHandler responseHandler) {
        post(null, url, null, responseHandler);
    }

    /**
     * 执行一个带参数的http post请求
     *
     * @param url             the URL to send the request to.
     * @param params          发送参数
     * @param responseHandler 响应回调
     */
    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        post(null, url, params, responseHandler);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param params          additional POST parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        post(context, url, paramsToEntity(params), null, responseHandler);
    }

    /**
     * 表单发送方式
     *
     * @param context
     * @param url
     * @param params
     * @param contentType
     * @param responseHandler
     */
    public void postByForm(Context context, String url, RequestParams params, String contentType, AsyncHttpResponseHandler responseHandler) {
        post(context, url, params.getFormDataEntity(), contentType, responseHandler);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated the request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param entity          a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType     the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void post(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, responseHandler, context);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated
     * the request. Set headers only for this request
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param headers         set headers only for this request
     * @param params          additional POST parameters to send with the request.
     * @param contentType     the content type of the payload you are sending, for
     *                        example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle
     *                        the response.
     */
    public void post(Context context, String url, Header[] headers, RequestParams params, String contentType,
                     AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = new HttpPost(url);
        if (params != null) request.setEntity(paramsToEntity(params));
        if (headers != null) request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType,
                responseHandler, context);
    }

    /**
     * Perform a HTTP POST request and track the Android Context which initiated
     * the request. Set headers only for this request
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param headers         set headers only for this request
     * @param entity          a raw {@link HttpEntity} to send with the request, for
     *                        example, use this to send string/json/xml payloads to a server by
     *                        passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType     the content type of the payload you are sending, for
     *                        example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle
     *                        the response.
     */
    public void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
                     AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null) request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, responseHandler, context);
    }
//
    // HTTP PUT Requests
    //

    /**
     * Perform a HTTP PUT request, without any parameters.
     *
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(String url, AsyncHttpResponseHandler responseHandler) {
        put(null, url, null, responseHandler);
    }

    /**
     * Perform a HTTP PUT request with parameters.
     *
     * @param url             the URL to send the request to.
     * @param params          additional PUT parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        put(null, url, params, responseHandler);
    }

    /**
     * Perform a HTTP PUT request and track the Android Context which initiated the request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param params          additional PUT parameters or files to send with the request.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        put(context, url, paramsToEntity(params), null, responseHandler);
    }

    /**
     * Perform a HTTP PUT request and track the Android Context which initiated the request.
     * And set one-time headers for the request
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param entity          a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType     the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(Context context, String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPut(url), entity), contentType, responseHandler, context);
    }

    /**
     * Perform a HTTP PUT request and track the Android Context which initiated the request.
     * And set one-time headers for the request
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param headers         set one-time headers for this request
     * @param entity          a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
     * @param contentType     the content type of the payload you are sending, for example application/json if sending a json payload.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void put(Context context, String url, Header[] headers, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPut(url), entity);
        if (headers != null) request.setHeaders(headers);
        sendRequest(httpClient, httpContext, request, contentType, responseHandler, context);
    }


    //
    // HTTP DELETE Requests
    //

    /**
     * Perform a HTTP DELETE request.
     *
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void delete(String url, AsyncHttpResponseHandler responseHandler) {
        delete(null, url, responseHandler);
    }

    /**
     * Perform a HTTP DELETE request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void delete(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        final HttpDelete delete = new HttpDelete(url);
        sendRequest(httpClient, httpContext, delete, null, responseHandler, context);
    }

    /**
     * Perform a HTTP DELETE request.
     *
     * @param context         the Android Context which initiated the request.
     * @param url             the URL to send the request to.
     * @param headers         set one-time headers for this request
     * @param responseHandler the response handler instance that should handle the response.
     */
    public void delete(Context context, String url, Header[] headers, AsyncHttpResponseHandler responseHandler) {
        final HttpDelete delete = new HttpDelete(url);
        if (headers != null) delete.setHeaders(headers);
        sendRequest(httpClient, httpContext, delete, null, responseHandler, context);
    }


    /**
     * 发送http请求
     *
     * @param client
     * @param httpContext
     * @param uriRequest
     * @param contentType
     * @param responseHandler
     * @param context
     */
    protected void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, AsyncHttpResponseHandler responseHandler, Context context) {
        //添加文件头
        if (contentType != null) {
            uriRequest.addHeader("Content-Type", contentType);
        }

        Future<?> request = threadPool.submit(new AsyncHttpRequest(client, httpContext, uriRequest, responseHandler));

        if (context != null) {
            // Add request to request map
            List<WeakReference<Future<?>>> requestList = requestMap.get(context);
            if (requestList == null) {
                requestList = new LinkedList<WeakReference<Future<?>>>();
                requestMap.put(context, requestList);
            }
            requestList.add(new WeakReference<Future<?>>(request));
        }
    }

    /**
     * 根据传入参数进行http地址请求转换
     *
     * @param url
     * @param params
     * @return
     */
    public static String getUrlWithQueryString(String url, RequestParams params) {
        if (params != null) {
            String paramString = params.getParamString();
            url += "?" + paramString;
        }
        return url;
    }


    /**
     * 将RequestParams转化为HttpEntity
     *
     * @param params
     * @return
     */
    private HttpEntity paramsToEntity(RequestParams params) {
        HttpEntity entity = null;
        if (params != null) {
            entity = params.getEntity();
        }
        return entity;
    }

    /**
     * 为request请求添加entity
     * @param requestBase
     * @param entity
     * @return
     */
    /**
     * @param requestBase
     * @param entity
     * @return
     */
    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
        if (entity != null) {
            requestBase.setEntity(entity);
        }
        return requestBase;
    }

    /**
     * 转换zip流为一般数据流
     *
     * @author zhao
     */
    private static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }
    }
}
