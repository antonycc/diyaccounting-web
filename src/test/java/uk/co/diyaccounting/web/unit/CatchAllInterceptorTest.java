package uk.co.diyaccounting.web.unit;

import jakarta.servlet.http.HttpServletRequest;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.web.controller.CatchAllInterceptor;

//import javax.servlet.http.HttpServletRequest;

/**
 * Tests the default handler
 * 
 * @author antony
 */
public class CatchAllInterceptorTest {

   @Test
   public void expectPreHandleToReturnFalse() throws Exception {

      // Test parameters
      String url = "httop://diyaccounting.co.uk";
      boolean expectedContinueProcessing = false;

      // Expected data
      StringBuffer requestUrl = new StringBuffer();
      requestUrl.append(url);

      // Mocks
      HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
      EasyMock.expect(request.getRequestURL()).andReturn(requestUrl).anyTimes();
      EasyMock.replay(request);

      // Class under test
      CatchAllInterceptor classUnderTest = new CatchAllInterceptor();

      // Execute
      boolean actualContinueProcessing = classUnderTest.preHandle(request, null, null);
      Assert.assertEquals(expectedContinueProcessing, actualContinueProcessing);
   }

   @Test
   public void expectHandleToReturnNull() throws Exception {

      // Test parameters
      String url = "httop://diyaccounting.co.uk";

      // Expected data
      StringBuffer requestUrl = new StringBuffer();
      requestUrl.append(url);

      // Mocks
      HttpServletRequest request = EasyMock.createNiceMock(HttpServletRequest.class);
      EasyMock.expect(request.getRequestURL()).andReturn(requestUrl).anyTimes();
      EasyMock.replay(request);

      // Class under test
      CatchAllInterceptor classUnderTest = new CatchAllInterceptor();

      // Execute
      boolean continueChain = classUnderTest.preHandle(request, null, null);
      Assert.assertFalse(continueChain);
   }
}
