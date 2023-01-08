package uk.co.diyaccounting.web.unit;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.diyaccounting.web.catalogue.Bundle;
import uk.co.diyaccounting.web.catalogue.CommercialProduct;
import uk.co.diyaccounting.web.service.CalculationUtils;

/**
 * Test key VAT calculations
 * 
 * @author Antony
 */
public class CalcTest {

   private static final Logger logger = LoggerFactory.getLogger(CalcTest.class);

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CalcTest.class);

   /**
    * Obtain the amount of sales tax to be paid upon purchase of an item The tax amount and catalogue product price are
    * assumed to be in the same local currency
    */
   @Test
   public void testTaxRounding() {

      // Test parameters
      float gross = 14.39f;
      float vatRate = 20.0f;

      // Expected results
      String expectedTax = "2.4";
      Bundle bundle = new Bundle();
      bundle.setRetailPrice(gross);
      CommercialProduct commercialProduct = new CommercialProduct();
      commercialProduct.setBundle(bundle);

      // Class Under test
      CalculationUtils classUnderTest = new CalculationUtils();

      // Execute
      float roundedTax = classUnderTest.getTaxAmount(vatRate, commercialProduct);
      String actualTax = Float.toString(roundedTax);

      // Check
      // CalcTest.logger.debug("price: [{}]", actualTax);
      Assert.assertEquals("Should be equal", expectedTax, actualTax);
   }

   /**
    * Obtain the amount of sales tax to be paid upon purchase of an item The tax amount and catalogue product price are
    * assumed to be in the same local currency
    * 
    * @param commercialProduct
    *           the product to obtain the tax for
    */
   @Test
   public void testNetCalc() {

      // Test parameters
      float gross = 20.0f;
      float vatRate = 20.0f;

      // Expected results
      String expectedNet = "16.67";
      Bundle bundle = new Bundle();
      bundle.setRetailPrice(gross);
      CommercialProduct commercialProduct = new CommercialProduct();
      commercialProduct.setBundle(bundle);

      // Class Under test
      CalculationUtils classUnderTest = new CalculationUtils();

      // Execute
      float net = classUnderTest.getNetAmount(vatRate, commercialProduct);
      String actualNet = Float.toString(net);

      // Check
      // CalcTest.logger.debug("price: [{}]", actualNet);
      Assert.assertEquals("Should be equal", expectedNet, actualNet);
   }
}
