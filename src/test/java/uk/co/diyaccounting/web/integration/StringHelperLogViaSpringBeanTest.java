package uk.co.diyaccounting.web.integration;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.polycode.mdcms.util.io.StringHelper;

import java.util.UUID;

/**
 * Check if the logging framework is active in the main classes
 * (Manual check for now)
 * 
 * @author Antony
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:/spring-context.xml" })
@SpringBootTest
public class StringHelperLogViaSpringBeanTest {

   @Autowired
   @Qualifier("string")
   private StringHelper string;

   @Test
   public void expectStringToBeLoggedAtInfo() {

      // Test parameters
      String s1 = UUID.randomUUID().toString();

      // Class under test
      StringHelper classUnderTest = this.string;

      // Execute
      Logger loggerUnderTest = classUnderTest.logString(s1);
      Assert.assertTrue("Info should be at least enabled", loggerUnderTest.isInfoEnabled());
   }
}