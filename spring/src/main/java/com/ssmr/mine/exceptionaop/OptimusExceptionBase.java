package com.ssmr.mine.exceptionaop;

public class OptimusExceptionBase extends RuntimeException{
    private static final long serialVersionUID = 1L;

    /**
     * 异常对应的http码
     */
    private int httpCode = 500;

    /**
     * 错误码
     */
    private String errCode = "error";

    /**
     * 是否异常型错误，true-表示系统故障或数据出现异常 false-表示正常逻辑的失败分支
     */
    private boolean fault;

    /**
     * 链路追踪id
     */
    private String traceId;

    /**
     * 产生异常的应用名
     */
    private String appName;

    public OptimusExceptionBase(){
        setupTraceInfo();
    }

    public OptimusExceptionBase(int httpCode, String errCode) {
        this.httpCode = httpCode;
        this.errCode = errCode;
        setupTraceInfo();
    }

    public OptimusExceptionBase(String errCode, String errorMsg) {
        super(errorMsg);
        this.errCode = errCode;
        setupTraceInfo();
    }

    public OptimusExceptionBase(int httpCode, String errCode, String errorMsg, Throwable e) {
        super(errorMsg, e);
        this.httpCode = httpCode;
        this.errCode = errCode;
        setupTraceInfo();
    }

    public OptimusExceptionBase(int httpCode, String errCode, String errorMsg) {
        super(errorMsg);
        this.httpCode = httpCode;
        this.errCode = errCode;
        setupTraceInfo();
    }

    public OptimusExceptionBase(String errorMsg) {
        super(errorMsg);
        setupTraceInfo();
    }

    public OptimusExceptionBase(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        setupTraceInfo();
    }

    public OptimusExceptionBase(Throwable cause) {
        super(cause);
        setupTraceInfo();
    }

    protected void setupTraceInfo() {
        this.traceId = "123";
        this.appName = "appname";
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public boolean isFault() {
        return fault;
    }

    public void setFault(boolean isFault) {
        this.fault = isFault;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getAppName() {
        return appName;
    }
}
