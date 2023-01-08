package uk.co.diyaccounting.web.unit;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockRequestDispatcher;
import uk.co.diyaccounting.web.controller.RootUrlIntercepter;

/**
 * Tests the root interceptor
 * 
 * @author antony
 */
public class RootUrlInterceptorTest {

   @Test
   public void expectPreHandleToReturnTrue() throws Exception {

      // Test parameters
      String inboundPath = "/home.html";
      boolean expectedContinueProcessing = true;

      // Expected data
      HttpServletResponse response = new MockHttpServletResponse();
      RequestDispatcher requestDispatcher = new MockRequestDispatcher(inboundPath);

      // Mocks
      HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
      EasyMock.expect(request.getServletPath()).andReturn(inboundPath).anyTimes();
      EasyMock.expect(request.getRequestDispatcher(inboundPath)).andReturn(requestDispatcher).anyTimes();
      EasyMock.replay(request);

      // Class under test
      RootUrlIntercepter classUnderTest = new RootUrlIntercepter();

      // Execute
      boolean actualContinueProcessing = classUnderTest.preHandle(request, response, null);
      Assert.assertEquals(expectedContinueProcessing, actualContinueProcessing);
   }

}
