package com.sinosoft.microservice.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 记录访问记录
 * 
 * @author 王涛
 *
 *         2017年8月23日11:05:28
 */
@Aspect
@Component
public class HttpAspect {

	private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

	@Pointcut("execution(public * com.sinosoft.microservice.*.controller.*.*(..))")
	public void log() {

	}

	@Before("log()")
	public void doBefore(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		logger.info("======================公共类打印日志START=================");
		// url
		logger.info("url={}", request.getRequestURL());

		// method
		logger.info("method={}", request.getMethod());

		// ip
		logger.info("ip={}", request.getRemoteAddr());

		// 类方法
		logger.info("class_method={}",
				joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		// 参数
		logger.info("args={}", joinPoint.getArgs());
	}

	@After("log()")
	public void doAfter() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// ip
		logger.info("url={},ip={},method={}", request.getRequestURL(), request.getRemoteAddr(), request.getMethod() + " 访问完成");
	}

	/**
	 * 返回结果时 Response 打印放回结果
	 * 
	 * @param object
	 */
	@AfterReturning(returning = "object", pointcut = "log()")
	public void doAfterReturning(Object object) {
		//判断返回值是否为空
		if (object != null) {
			logger.info("response={}", object.toString());
		} else {
			logger.info("返回值为空,response={}", object);
		}
		logger.info("======================公共类打印日志END=================");
	}

}
