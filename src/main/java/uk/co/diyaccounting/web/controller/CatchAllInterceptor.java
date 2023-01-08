package uk.co.diyaccounting.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Maps in bound request to the home page
 */
@Component("catchAllInterceptor")
public class CatchAllInterceptor implements HandlerInterceptor {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(CatchAllInterceptor.class);

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
    * javax.servlet.http.HttpServletResponse, java.lang.Object)
    */
   //@Override
   public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object
            handler) throws Exception {
      String path = request.getRequestURL().toString();
      CatchAllInterceptor.logger.debug("preHandle: {}", path);
      return false;
   }

}
