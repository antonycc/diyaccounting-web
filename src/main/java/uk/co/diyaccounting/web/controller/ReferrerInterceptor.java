package uk.co.diyaccounting.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.co.diyaccounting.web.model.Site;

/**
 * Locates referrer codes in a request and stores them in the cookie
 */
@Component("referrerInterceptor")
public class ReferrerInterceptor implements HandlerInterceptor {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(ReferrerInterceptor.class);

	/**
	 * The site properties
	 */
	@Autowired
	@Qualifier("site")
	private Site site;

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
      ReferrerInterceptor.logger.debug("preHandle: {}", path);

      // look for a referrer parameter
      String referrerCode = request.getParameter("referrer");
      if (StringUtils.isBlank(referrerCode)) {
         referrerCode = request.getParameter("referer");
      }

      return true;
   }


	/**
	 * The site specific properties service
	 *
	 * @param site
	 *           the site to set
	 */
	public void setSite(final Site site) {
		this.site = site;
	}
}
