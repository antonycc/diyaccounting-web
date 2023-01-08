package uk.co.diyaccounting.web.content;

import org.apache.commons.lang3.ArrayUtils;
import uk.co.polycode.mdcms.cms.dto.Metadata;
import uk.co.polycode.mdcms.cms.type.MdContent;

/**
 * Product attributes
 * 
 * @author Antony
 */
public class Product extends Metadata {

   private static final long serialVersionUID = 1L;

   @MdContent(path = "/featureNames", trim = true)
   private String[] featureNames;

   @MdContent(path = "/from", trim = true)
   private float from;

   /**
    * Bundles for this item
    */
   private String[] bundleNames;

   /**
    * The fulfillment url for use where the product is not fulfilled internally
    */
   @MdContent(path = "/fulfillmentUrl", trim = true)
   private String fulfillmentUrl;

   /**
    * Google AdWords conversion code for this item
    */
   private String conversionCode;

   @Override
   public String getExtension(){
      return ".md";
   }

   @Override
   public String getFilter(){
      return "Product" + this.getExtension();
   }

   @Override
   public void postPopulationConfig() {
      super.postPopulationConfig();
   }

   public String[] getFeatureNames() {
      return ArrayUtils.clone(this.featureNames);
   }

   public void setFeatureNames(final String[] featureNames) {
      this.featureNames = ArrayUtils.clone(featureNames);
   }

   public float getFrom() {
      return this.from;
   }

   public void setFrom(final float from) {
      this.from = from;
   }

   public String[] getBundleNames() {
      return ArrayUtils.clone(this.bundleNames);
   }

   public void setBundleNames(final String[] bundleNames) {
      this.bundleNames = ArrayUtils.clone(bundleNames);
   }

   public String getConversionCode() {
      return this.conversionCode;
   }

   public void setConversionCode(final String conversionCode) {
      this.conversionCode = conversionCode;
   }

   public String getFulfillmentUrl() {
      return this.fulfillmentUrl;
   }

   public void setFulfillmentUrl(final String fulfillmentUrl) {
      this.fulfillmentUrl = fulfillmentUrl;
   }
}
