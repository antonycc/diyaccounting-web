package uk.co.diyaccounting.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Logs a line of debug to show it's presence and always returns true.
 */
@Component("debugInterceptor")
public class LoggingInterceptor implements HandlerInterceptor {

	/**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

	/**
	 * A logging proxy to allow mocking of the logger
	 */
	private ProxyLogger proxyLogger = new ProxyLogger();

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
    * javax.servlet.http.HttpServletResponse, java.lang.Object)
    */
   //@Override
   public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
      String path = request.getServletPath();
	   this.proxyLogger.debug(logger, "[Pre-handle] processing: " + path);
      return true;
   }

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	//@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, ModelAndView modelAndView)
		throws Exception {
		String path = request.getServletPath();
		this.proxyLogger.debug(logger, "[Post-handle] processed: " + path);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,java.lang.Exception)
	 */
	//@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, Exception exception)
		throws Exception {
		String path = request.getServletPath();
		if(exception != null){
			String message = "[After Completion] exception: " + path;
			this.proxyLogger.error(logger, message, exception);
		}else {
			this.proxyLogger.info(logger, "[After Completion] completed: " + path);
		}
	}

	/**
	 * Set the proxy logger (only used for mocking)
	 *
	 * @param proxyLogger the logger to set
	 */
	public void setProxyLogger(ProxyLogger proxyLogger) {
		this.proxyLogger = proxyLogger;
	}

	/**
	 * Get the static logger for the class
	 *
	 * @return the static logger for this class
	 */
	public static Logger getLogger() {
		return logger;
	}
}
