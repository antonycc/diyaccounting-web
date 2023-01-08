package uk.co.diyaccounting.web.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.web.content.Page;
import uk.co.diyaccounting.web.content.Product;
import uk.co.polycode.mdcms.cms.service.ContentException;

import java.util.UUID;

/**
 * Tests for the product DTOs
 * 
 * @author antony
 */
public class ProductDTOTest {

   @Test
   public void expectBundleNameExtensionsToBeStripped() throws ContentException {

      // Test parameters
      String productName = "CompanyAccounts";
      String bundleName = "Payroll";
      String extension = ".do";
      String name = "some name which should not override the headline product";
      double from = 0.0d;
      String conversionCode = UUID.randomUUID().toString();

      // Expected data
      String bundleNameWithExtension = bundleName + extension;
      String[] bundleNames = {bundleNameWithExtension};

      // Class under test
      Product classUnderTest = new Product();
      classUnderTest.setFeatureNames(new String[0]);
      classUnderTest.setBundleNames(bundleNames);
      //classUnderTest.setHeadlineProductName(productName);
      classUnderTest.setName(name);
      classUnderTest.setConversionCode(conversionCode);

      // Execute
      classUnderTest.postPopulationConfig();

      // Check
      //Assert.assertEquals(bundleName, classUnderTest.getBundleNames()[0]);
      //Assert.assertEquals(productName, classUnderTest.getHeadlineProductName());
      Assert.assertTrue(from == classUnderTest.getFrom());
      Assert.assertEquals(conversionCode, classUnderTest.getConversionCode());
   }

   @Test
   public void expectPageAttributesToMatchGettersAndSetters() throws ContentException {

      // Test parameters
      String type = UUID.randomUUID().toString();

      // Expected data

      // Class under test
      Page classUnderTest = new Page();
      //classUnderTest.setType(type);

      // Execute
      classUnderTest.postPopulationConfig();

      // Check
      //Assert.assertEquals(type, classUnderTest.getType());
   }
}
