package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;
import java.util.UUID;

/**
 * A product that can be purchased by an end user.
 * 
 * @author Antony
 */
public class ProductComponent extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   /**
    * The query for obtaining the accounting periods for a given product component
    */
   @JsonIgnore
   public static final String FIND_ACCOUNTING_PERIODS = "findAccountingPeriods";

   /**
    * The query for obtaining the product component from its constant parts
    */
   @JsonIgnore
   public static final String FIND_PRODUCT_COMPONENT = "findProductComponent";

	/**
	 * The query for obtaining all the product components for a product type
	 */
	@JsonIgnore
	public static final String FIND_PRODUCT_COMPONENTS_FOR_PRODUCT_TYPE = "findProductComponentsForProductType";

	/**
    * The query for obtaining the product types for a region
    */
   @JsonIgnore
   public static final String FIND_PRODUCT_TYPES = "findProductTypes";

   private UUID id;

   /**
    * The region this product is designed to address the needs of.
    */
   private Region region;

   /**
    * The type of product this commercial product is an instance of.
    */
   private ProductType productType;

   /**
    * The accounting period this commercial product spans.
    */
   private AccountingPeriod accountingPeriod;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public ProductComponent(){
   }

   /**
    * A string version including the unique attribute(s) whick make up the primary key of this entity
    * 
    * @return A string version of this entity
    */
   @Override
   public String toString() {
      StringBuilder buf = new StringBuilder();
      buf.append("{");
      buf.append(this.getClass().getSimpleName());
      buf.append(":");
      buf.append(this.getRegion() != null ? this.getRegion().toString() : "null");
      buf.append(",");
      buf.append(this.getProductType() != null ? this.getProductType().toString() : "null");
      buf.append(",");
      buf.append(this.getAccountingPeriod() != null ? this.getAccountingPeriod().toString() : "null");
      buf.append("}");
      return buf.toString();
   }

   /**
    * The primary key of the entity
    * 
    * @return the id
    */
   public UUID getId() {
      return this.id;
   }

   /**
    * The primary key of the entity
    * 
    * @param id
    *           the id to set
    */
   public void setId(final UUID id) {
      this.id = id;
   }

   /**
    * The region this product is designed to address the needs of.
    * 
    * @return the region
    */
   public Region getRegion() {
      return this.region;
   }

   /**
    * The region this product is designed to address the needs of.
    * 
    * @param region
    *           the region to set
    */
   public void setRegion(final Region region) {
      this.region = region;
   }

   /**
    * The type of product this commercial product is an instance of.
    * 
    * @return the productType
    */
   public ProductType getProductType() {
      return this.productType;
   }

   /**
    * The type of product this commercial product is an instance of.
    * 
    * @param productType
    *           the productType to set
    */
   public void setProductType(final ProductType productType) {
      this.productType = productType;
   }

   /**
    * The accounting period this commercial product spans.
    * 
    * @return the accountingPeriod
    */
   public AccountingPeriod getAccountingPeriod() {
      return this.accountingPeriod;
   }

   /**
    * The accounting period this commercial product spans.
    * 
    * @param accountingPeriod
    *           the accountingPeriod to set
    */
   public void setAccountingPeriod(final AccountingPeriod accountingPeriod) {
      this.accountingPeriod = accountingPeriod;
   }
}