package org.rockyang.blockj.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 返回 Json 字符串 VO
 *
 * @author yangjian
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonVo<T>
{

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    // version
    private int version = 1;
    // status code
    private int code;
    // error message
    private String message;
    private T data;

    public JsonVo()
    {
    }

    public JsonVo(int code, T data)
    {
        this.code = code;
        this.data = data;
    }

    public JsonVo(int code, String message, T data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JsonVo(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public JsonVo setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public int getVersion()
    {
        return version;
    }

    public JsonVo setVersion(int version)
    {
        this.version = version;
        return this;
    }

    public T getData()
    {
        return data;
    }

    public JsonVo setData(T data)
    {
        this.data = data;
        return this;
    }

    public boolean isOK()
    {
        return code == SUCCESS;
    }
}
