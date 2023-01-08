package uk.co.diyaccounting.web.controller;

import org.slf4j.Logger;

/**
 * Logs a line of debug.
 */
public class ProxyLogger{

   public void debug(final Logger logger, String message)
            throws Exception {
      logger.debug(message);
   }

	public void info(final Logger logger, String message)
			throws Exception {
		logger.info(message);
	}

	/**
	 * Log a line of debug to the provided logger
	 */
	public void error(final Logger logger, String message, Throwable throwable){
		logger.error(message, throwable);
	}

}
