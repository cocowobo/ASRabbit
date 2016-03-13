
package com.ht.baselib.helper.download;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Msg:Http响应类
 * Update:  2015-10-28
 * Version: 1.0
 * Created by laijiacai on 2015-10-28 11:19.
 */
public class KGHttpResponse {

    private int responseCode;

    private InputStream inputStream;

    private Map<String, Object> headers;

    public static final String CONTENT_LENGTH = "content_length";

    public static final String CONTENT_TYPE = "content_type";

    public int getResponseCode() {
        return responseCode;
    }

    public KGHttpResponse(int responseCode, InputStream inputStream) {
        this.responseCode = responseCode;
        this.inputStream = inputStream;
        this.headers = new HashMap<String, Object>();
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    /**
     *
     * @param name 数据头key
     * @return
     */
    public Object getHeader(String name) {
        return headers.get(name);
    }

    /**
     *
     * @param name 数据头key
     * @param value 数据头value
     */
    public void setHeader(String name, Object value) {
        this.headers.put(name, value);
    }

    /**
     *
     * @param name 数据头key
     * @return
     */
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

}
