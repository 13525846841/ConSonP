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

import com.yksj.healthtalk.utils.LogUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * http请求处理线程
 *
 * @author zhao
 */
class AsyncHttpRequest implements Runnable {
    private final AbstractHttpClient client;
    private final HttpContext context;
    private final HttpUriRequest request;
    private final AsyncHttpResponseHandler responseHandler;
    private boolean isBinaryRequest;

    private int executionCount;

    public AsyncHttpRequest(AbstractHttpClient client, HttpContext context,
                            HttpUriRequest request, AsyncHttpResponseHandler responseHandler) {
        this.client = client;
        this.context = context;
        this.request = request;
        this.responseHandler = responseHandler;
        if (responseHandler instanceof BinaryHttpResponseHandler) {
            this.isBinaryRequest = true;
        }
        if (LogUtil.DEBUG) {
            LogUtil.d("HEALTHTALK_HTTP", request.getURI().toString());
        }
    }

    public void run() {
        try {
            if (responseHandler != null) {
                responseHandler.sendStartMessage();
            }
            makeRequestWithRetries();

            if (responseHandler != null) {
                responseHandler.sendFinishMessage();
            }
        } catch (Exception e) {
            if (responseHandler != null) {
                responseHandler.sendFinishMessage();
                if (this.isBinaryRequest) {
                    responseHandler.sendFailureMessage(e, (byte[]) null);
                } else {
                    responseHandler.sendFailureMessage(e, (String) null);
                }
            }
        }
    }

    private void makeRequestWithRetries() throws ConnectException {
        // This is an additional layer of retry logic lifted from droid-fu
        // See:
        // https://github.com/kaeppler/droid-fu/blob/master/src/main/java/com/github/droidfu/http/BetterHttpRequestBase.java

        boolean retry = true;// 标记连续执行多次
        IOException cause = null;
        HttpRequestRetryHandler retryHandler = client
                .getHttpRequestRetryHandler();
        while (retry) {
            try {
                makeRequest();
                return;
            } catch (UnknownHostException e) {
                if (responseHandler != null) {
                    responseHandler.sendFailureMessage(e, "can't resolve host");
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
                cause = e;
                retry = retryHandler.retryRequest(cause, ++executionCount, context);
//				if (responseHandler != null) {
//					responseHandler.sendFailureMessage(e, "can't resolve host");
//				}
            } catch (NullPointerException e) {
                e.printStackTrace();
                // there's a bug in HttpClient 4.0.x that on some occasions
                // causes
                // DefaultRequestExecutor to throw an NPE, see
                // http://code.google.com/p/android/issues/detail?id=5255
                cause = new IOException("NPE in HttpClient" + e.getMessage());
                retry = retryHandler.retryRequest(cause, ++executionCount,
                        context);// 进行http连接重试
            } catch (Exception e) {
                if (responseHandler != null) {
                    responseHandler.sendFailureMessage(e, e.getMessage());
                }
                return;
            }
        }
        // no retries left, crap out with exception
        ConnectException ex = new ConnectException();
        ex.initCause(cause);
        throw ex;
    }

    /**
     * 执行一个http请求
     *
     * @throws IOException
     */
    private void makeRequest() throws Exception {
        if (!Thread.currentThread().isInterrupted()) {// 当前线程是否被终止
            HttpResponse response = client.execute(request, context);
            if (!Thread.currentThread().isInterrupted()) {
                if (responseHandler != null) {
                    responseHandler.sendResponseMessage(response);
                }
            } else {
                // TODO: should raise InterruptedException? this block is
                // reached whenever the request is cancelled before its response
                // is received
            }
        }
    }


}
