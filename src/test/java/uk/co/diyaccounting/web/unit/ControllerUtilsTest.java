package uk.co.diyaccounting.web.unit;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import uk.co.diyaccounting.web.controller.ControllerUtils;

import java.util.UUID;

/**
 * Tests the translation part of controller utils
 * 
 * @author antony
 */
public class ControllerUtilsTest {

   @Test(expected = RuntimeException.class)
   public void expectRuntimeExceptionWhenUnknownAlgorithmIsUsed() {

      // Test parameters
      String algorithm = "no.such.algorithm";
      byte[] bytes = UUID.randomUUID().toString().getBytes();

      // Expected data

      // Mocks

      // Class under test
      ControllerUtils classUnderTest = new ControllerUtils();

      // Execute
      classUnderTest.getDigest(algorithm, bytes);

      // Checks
   }

   @Test
   public void expectNoHeadersToBeSetForNullResponse() {

      // Test parameters

      // Expected data
      ModelAndView modelAndView = new ModelAndView();

      // Mocks

      // Class under test
      ControllerUtils classUnderTest = new ControllerUtils();

      // Execute
      classUnderTest.setHeaders(null, modelAndView, null);

      // Checks
      Assert.assertEquals(0, modelAndView.getModelMap().size());
   }
}
