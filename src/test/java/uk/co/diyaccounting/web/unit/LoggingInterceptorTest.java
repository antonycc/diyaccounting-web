package uk.co.diyaccounting.web.unit;

import jakarta.servlet.http.HttpServletRequest;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.co.diyaccounting.web.controller.LoggingInterceptor;
import uk.co.diyaccounting.web.controller.ProxyLogger;

/**
 * Tests the debug interceptor
 * 
 * @author antony
 */
public class LoggingInterceptorTest {

	private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptorTest.class);

   @Test
   public void expectDebugInterceptorToDebugPreHandle()
            throws Exception {

      // Test parameters
      String path = "Company-Accounts-Confirmed.do";
	   Logger logger = LoggingInterceptor.getLogger();

      // Mocks
      HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
      MockHttpServletResponse response = new MockHttpServletResponse();
      EasyMock.expect(request.getServletPath()).andReturn(path);
	   ProxyLogger proxyLogger = EasyMock.createStrictMock(ProxyLogger.class);
	   proxyLogger.debug(logger, "[Pre-handle] processing: " + path);
	   EasyMock.expectLastCall();
      EasyMock.replay(request, proxyLogger);

      // Class under test
      LoggingInterceptor classUnderTest = new LoggingInterceptor();
		classUnderTest.setProxyLogger(proxyLogger);

      // Execute
      classUnderTest.preHandle(request, response, null);

      // Checks
   }

	@Test
	public void expectDebugInterceptorToDebugPostHandle()
		throws Exception {

		// Test parameters
		String path = "Company-Accounts-Confirmed.do";
		Logger logger = LoggingInterceptor.getLogger();

		// Mocks
		HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
		MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(request.getServletPath()).andReturn(path);
		ProxyLogger proxyLogger = EasyMock.createStrictMock(ProxyLogger.class);
		proxyLogger.debug(logger, "[Post-handle] processed: " + path);
		EasyMock.expectLastCall();
		EasyMock.replay(request, proxyLogger);

		// Class under test
		LoggingInterceptor classUnderTest = new LoggingInterceptor();
		classUnderTest.setProxyLogger(proxyLogger);

		// Execute
		classUnderTest.postHandle(request, response, null, null);

		// Checks
	}

	@Test
	public void expectDebugInterceptorToDebugAfterCompletion()
		throws Exception {

		// Test parameters
		String path = "Company-Accounts-Confirmed.do";
		Logger logger = LoggingInterceptor.getLogger();

		// Mocks
		HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
		MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(request.getServletPath()).andReturn(path);
		ProxyLogger proxyLogger = EasyMock.createStrictMock(ProxyLogger.class);
		proxyLogger.info(logger, "[After Completion] completed: " + path);
		EasyMock.expectLastCall();
		EasyMock.replay(request, proxyLogger);

		// Class under test
		LoggingInterceptor classUnderTest = new LoggingInterceptor();
		classUnderTest.setProxyLogger(proxyLogger);

		// Execute
		classUnderTest.afterCompletion(request, response, null, null);

		// Checks
	}

	@Test
	public void expectDebugInterceptorToDebugAfterCompletionForException()
		throws Exception {

		// Test parameters
		String path = "Company-Accounts-Confirmed.do";
		Logger logger = LoggingInterceptor.getLogger();
		Exception exception = new Exception("Test Exception");

		// Mocks
		HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
		MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(request.getServletPath()).andReturn(path);
		ProxyLogger proxyLogger = EasyMock.createStrictMock(ProxyLogger.class);
		proxyLogger.error(logger, "[After Completion] exception: " + path, exception);
		EasyMock.expectLastCall();
		EasyMock.replay(request, proxyLogger);

		// Class under test
		LoggingInterceptor classUnderTest = new LoggingInterceptor();
		classUnderTest.setProxyLogger(proxyLogger);

		// Execute
		classUnderTest.afterCompletion(request, response, null, exception);

		// Checks
	}

	@Test
	public void proxyLoggingStabilityTest()
		throws Exception {

		// Test parameters
		String message = "log message";
		Exception exception = new Exception("Test exception");

		// Mocks

		// Class under test
		ProxyLogger classUnderTest = new ProxyLogger();

		// Execute
		classUnderTest.debug(LoggingInterceptorTest.logger, message);
		classUnderTest.error(LoggingInterceptorTest.logger, message, null);
		classUnderTest.error(LoggingInterceptorTest.logger, message, exception);

		// Checks
	}
}
