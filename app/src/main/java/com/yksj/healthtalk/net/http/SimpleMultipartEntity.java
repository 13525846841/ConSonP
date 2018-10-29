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

/*
    This code is taken from Rafael Sanches' blog.
    http://blog.rafaelsanches.com/2011/01/29/upload-using-multipart-post-using-httpclient-in-android/
*/

package com.yksj.healthtalk.net.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

/**
 * 文件上传实体
 * @author zhao
 *
 */
class SimpleMultipartEntity implements HttpEntity {
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private String boundary = null;
    
    private String PREFIX = "--", LINEND = "\r\n",CHARSET = "UTF-8";
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean isSetLast = false;
    boolean isSetFirst = false;

    public SimpleMultipartEntity() {
        final StringBuffer buf = new StringBuffer();
        final Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = buf.toString();
    }

    public void writeFirstBoundaryIfNeeds(){
        if(!isSetFirst){
            try {
                out.write(("--" + boundary + "\r\n").getBytes());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if(isSetLast){
            return;
        }

        try {
            out.write(("\r\n--" + boundary + "--\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }

        isSetLast = true;
    }

    /**
     * 添加参数
     * @param key
     * @param prame
     */
    public void addParame(String key ,String prame){
    	/*StringBuilder sb = new StringBuilder();
    	sb.append(PREFIX);
    	sb.append(boundary);
    	sb.append(LINEND);
    	sb.append("Content-Disposition: form-data; name=\""+ key + "\"" + LINEND);
    	sb.append("Content-Type: multipart/form-data; charset=" + CHARSET + LINEND);
    	sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
    	sb.append(LINEND);
    	sb.append(prame);
    	sb.append(LINEND);*/
//			out.write(sb.toString().getBytes());
    }
    
    public void addPartByForm(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" +key+"\"\r\n\r\n").getBytes());
            String value1 = URLEncoder.encode(value,HTTP.UTF_8);
            out.write(value1.getBytes());
            out.write("\r\n\r\n".getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addPart(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" +key+"\"\r\n\r\n").getBytes());
            String value1 = URLEncoder.encode(value,HTTP.UTF_8);
            out.write(value1.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
/*    public void addNullFile(){
    	String type = "Content-Type: "+""+"\r\n";
        try {
			out.write(("Content-Disposition: form-data; name=\""+ ""+"\"; filename=\"" + "" + "\"\r\n").getBytes());
			out.write(type.getBytes());
		    out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
		    out.write(("\r\n--" + boundary + "\r\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }*/

    public void addPart(final String key, final String fileName, final InputStream fin, final boolean isLast){
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    /**
     * 添加文件上传部分
     * @param key
     * @param fileName
     * @param fin
     * @param type
     * @param isLast
     */
    public void addPart(final String key, final String fileName, final InputStream fin, String type, final boolean isLast){
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: "+type+"\r\n";
            out.write(("Content-Disposition: form-data; name=\""+ key+"\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
            if(fin != null){
            	final byte[] tmp = new byte[4096];
                int l = 0;
                while ((l = fin.read(tmp)) != -1) {
                    out.write(tmp, 0, l);
                }
            }
            if(!isLast)
                out.write(("\r\n--" + boundary + "\r\n").getBytes());
            out.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fin != null)fin.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPart(final String key, final File value, final boolean isLast) {
        try {
            addPart(key, value.getName(), new FileInputStream(value), isLast);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return out.toByteArray().length;
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        outstream.write(out.toByteArray());
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public void consumeContent() throws IOException,
    UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
            "Streaming entity does not implement #consumeContent()");
        }
    }

    @Override
    public InputStream getContent() throws IOException,
    UnsupportedOperationException {
        return new ByteArrayInputStream(out.toByteArray());
    }
}