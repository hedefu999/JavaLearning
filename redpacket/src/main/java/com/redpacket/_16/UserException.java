package com.redpacket._16;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 通过code映射spring MVC的异常码，reason描述异常原因
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND,reason = "用户未找到")
public class UserException extends RuntimeException {
}
