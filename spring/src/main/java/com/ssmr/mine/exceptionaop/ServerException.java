package com.ssmr.mine.exceptionaop;


import java.text.MessageFormat;

public class ServerException extends OptimusExceptionBase {
	private static final long serialVersionUID = -9202495170920439226L;

	private final static int HTTP_CODE = 500;

	public ServerException(String exceptionMsg) {
		super(HTTP_CODE, exceptionMsg, exceptionMsg);
	}

	public ServerException(String exceptionMsg, Object... arguments) {
		super(HTTP_CODE, exceptionMsg, MessageFormat.format(exceptionMsg, arguments));
	}

	public ServerException(String exceptionMsg, Throwable throwable) {
		super(HTTP_CODE, exceptionMsg, exceptionMsg, throwable);
	}

	public ServerException(String exceptionMsg, Throwable throwable, Object... arguments) {
		super(HTTP_CODE, exceptionMsg, MessageFormat.format(exceptionMsg, arguments),
				throwable);
	}

	public ServerException(String exceptionMsg, String exceptionDes) {
		super(HTTP_CODE, exceptionMsg, exceptionDes);
	}

}
