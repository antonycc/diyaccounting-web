package uk.co.diyaccounting.web.service;

import org.springframework.stereotype.Service;
import uk.co.diyaccounting.web.catalogue.CommercialProduct;

/**
 * Handles requests for the application's simple request to view mappings
 */
@Service("calculationUtils")
public class CalculationUtils {

   /**
    * The denominator for percentages
    */
   static final float PERCENTAGE_BASE = 100.0f;

   /**
    * Obtain the catalogue product price for a commercial product
    * 
    * @param vatRate
    *           the VAT rate
    * @param commercialProduct
    *           the product to obtain the price for
    * @return the cost of the item
    */
   public float getNetAmount(final float vatRate, final CommercialProduct commercialProduct) {
      float gross = commercialProduct.getBundle().getRetailPrice();
      return this.getNetAmount(gross, vatRate);
   }

   /**
    * Obtain the amount of sales tax to be paid upon purchase of an item The tax amount and catalogue product price are
    * assumed to be in the same local currency
    * 
    * @param vatRate
    *           the VAT rate
    * @param commercialProduct
    *           the product to obtain the tax for
    * @return the tax amount in the local currency
    */
   public float getTaxAmount(final float vatRate, final CommercialProduct commercialProduct) {
      float gross = commercialProduct.getBundle().getRetailPrice();
      return this.getTaxAmount(gross, vatRate);
   }

   /**
    * Obtain the catalogue product price for a commercial product
    * 
    * @param gross
    *           the amount inclusive of VAT
    * @param vatRate
    *           the rate of VAt applied
    * @return the Net value after VAt has been deducted
    */
   public float getNetAmount(final float gross, final float vatRate) {
      float tax = this.getTaxAmount(gross, vatRate);
      float net = gross - tax;
      return Math.round(net * CalculationUtils.PERCENTAGE_BASE) / CalculationUtils.PERCENTAGE_BASE;
   }

   /**
    * Obtain the amount of sales tax to be paid upon purchase of an item The tax amount and catalogue product price are
    * assumed to be in the same local currency
    * 
    * @param gross
    *           the price of the product to obtain the tax for
    * @param vatRate
    *           the VAT rate
    * @return the tax amount in the local currency
    */
   public float getTaxAmount(final float gross, final float vatRate) {
      float fractionalVatRate = vatRate / CalculationUtils.PERCENTAGE_BASE;
      float tax = gross - (gross / (1 + fractionalVatRate));
      return Math.round(tax * CalculationUtils.PERCENTAGE_BASE) / CalculationUtils.PERCENTAGE_BASE;
   }

}
