package uk.co.diyaccounting.web.unit;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test the top level services behave as expected
 * 
 * @author Antony
 */
public class CatalogueServiceRegExTest {

   /**
    * Test regex string splitting
    * 
    * @throws IOException
    */
   @Test
   public void testRegEx() throws IOException {

      // Test parameters
      String delim = " ";
      String catalogueName = "Basic Sole Trader";
      String accountingPeriodName = "2008-04-05 (Apr08)";
      String productTypeToEnd = catalogueName + delim + accountingPeriodName;
      String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2} [(]{1}[A-Z]{1}[a-z]{2}[0-9]{2}[)]{1}";

      // Expected Results
      String expectedCatalogueName = catalogueName;
      String expectedAccountingPeriodName = accountingPeriodName;

      // Execute tests
      Pattern p = Pattern.compile(regex);
      String actualProductTypeCatalogueName = p.split(productTypeToEnd)[0].trim();
      Assert.assertEquals("Name should be as exepcted", expectedCatalogueName, actualProductTypeCatalogueName);
      // CatalogueServiceRegExTest.logger.debug("Got name: [{}]", actualProductTypeCatalogueName);

      Matcher matcher = p.matcher(productTypeToEnd);
      matcher.find();
      String actualAccountingPeriodName = productTypeToEnd.substring(matcher.start(), matcher.end());
      Assert.assertEquals("Period should be as exepcted", expectedAccountingPeriodName, actualAccountingPeriodName);
      // CatalogueServiceRegExTest.logger.debug("Got period: [{}]", actualAccountingPeriodName);
   }

}