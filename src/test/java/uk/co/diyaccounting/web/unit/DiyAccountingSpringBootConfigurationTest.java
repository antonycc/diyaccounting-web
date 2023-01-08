package uk.co.diyaccounting.web.unit;

import freemarker.template.TemplateException;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import uk.co.diyaccounting.web.DiyAccountingSpringBootConfiguration;
import uk.co.polycode.mdcms.cms.service.ContentException;

import java.io.IOException;

/**
 * Tests for the app config
 * 
 * @author antony
 */
public class DiyAccountingSpringBootConfigurationTest {

   @Test
   public void expectHandlerMappingToBeCreated() {

      // Test parameters

      // Expected data

      // Mocks

      // Class under test
      DiyAccountingSpringBootConfiguration classUnderTest = new DiyAccountingSpringBootConfiguration();

      // Execute
      AbstractHandlerMapping handlerMapping = classUnderTest.handlerMapping();

      // Checks
      Assert.assertNotNull(handlerMapping);
   }
/*
   @Test
   public void expectFreemarkerConfigurerToBeCreated() {

      // Test parameters

      // Expected data

      // Mocks

      // Class under test
      DiyAccountingSpringBootConfiguration classUnderTest = new DiyAccountingSpringBootConfiguration();

      // Execute
      FreeMarkerConfig freeMarkerConfigurer = classUnderTest.freeMarkerConfig();
      //Configuration configuration = classUnderTest.freemarkerConfig();

      // Checks
      Assert.assertNotNull(freeMarkerConfigurer);
      //Assert.assertNotNull(configuration);
   }
*/

   @Test
   public void expectViewResolverRegistryToDoNothing() throws ContentException, IOException, TemplateException {

      // Test parameters
      String templatePath = "/";

      // Expected data
      IOException ioe = new IOException("mock forced exception");

      // Mocks
      ViewResolverRegistry registry = EasyMock.createNiceMock(ViewResolverRegistry.class);
      EasyMock.replay(registry);

      // Class under test
      DiyAccountingSpringBootConfiguration classUnderTest = new DiyAccountingSpringBootConfiguration();

      // Execute
      classUnderTest.configureViewResolvers(registry);
      ViewResolver resolver = classUnderTest.viewResolver();

      // Checks
      Assert.assertNotNull(resolver);
   }
/*
   @Test(expected = IllegalArgumentException.class)
   public void expectIOException() throws ContentException, IOException, TemplateException {

      // Test parameters
      String templatePath = "/";

      // Expected data
      IOException ioe = new IOException("mock forced exception");

      // Mocks
      FreeMarkerConfigurer freeMarkerConfigurer = EasyMock.createNiceMock(FreeMarkerConfigurer.class);
      EasyMock.expect(freeMarkerConfigurer.createConfiguration()).andThrow(ioe);
      EasyMock.replay(freeMarkerConfigurer);

      // Class under test
      DiyAccountingSpringBootConfiguration classUnderTest = new DiyAccountingSpringBootConfiguration();

      // Execute
      classUnderTest.createConfiguration(freeMarkerConfigurer, templatePath);
      // Checks
   }

   @Test(expected = IllegalArgumentException.class)
   public void expectTemplateException() throws ContentException, IOException, TemplateException {

      // Test parameters
      String templatePath = "/";

      // Expected data

      // Mocks
      TemplateException te = EasyMock.createNiceMock(TemplateException.class);
      FreeMarkerConfigurer freeMarkerConfigurer = EasyMock.createNiceMock(FreeMarkerConfigurer.class);
      EasyMock.expect(freeMarkerConfigurer.createConfiguration()).andThrow(te);
      EasyMock.replay(freeMarkerConfigurer, te);

      // Class under test
      DiyAccountingSpringBootConfiguration classUnderTest = new DiyAccountingSpringBootConfiguration();

      // Execute
      classUnderTest.createConfiguration(freeMarkerConfigurer, templatePath);
      // Checks
   }
   */
}
