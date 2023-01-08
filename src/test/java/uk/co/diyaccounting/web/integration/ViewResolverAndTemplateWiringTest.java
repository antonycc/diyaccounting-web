package uk.co.diyaccounting.web.integration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ViewResolver;
import uk.co.diyaccounting.web.DiyAccountingSpringBootConfiguration;

/**
 * Test key elements of the application configuration are wired in
 * 
 * @author Antony
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:/spring-context.xml" })
@SpringBootTest
public class ViewResolverAndTemplateWiringTest {

   /**
    * Wire in the config - requires spring context
    */
   @Autowired
   @Qualifier("appConfigController")
   private DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration;

   @Test
   public void testHomeViewIsMapped() throws Exception {

      // Expected results
      String expectedUrl = "/view/testhome.ftl";

      // Ensure app config is created and contains view resolver
      Assert.assertNotNull(this.diyAccountingSpringBootConfiguration);

      // Check object state
      ViewResolver resolver = this.diyAccountingSpringBootConfiguration.viewResolver();
      Assert.assertNotNull(resolver);
   }

   @Test
   public void testTemplateConfigured() throws Exception {

      // Ensure app config is created
      Assert.assertNotNull(this.diyAccountingSpringBootConfiguration);

      // Ensure app config contains template view resolver
      Assert.assertNotNull(this.diyAccountingSpringBootConfiguration.viewResolver());
   }

}
