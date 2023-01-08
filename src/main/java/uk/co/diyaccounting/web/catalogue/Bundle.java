package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A bundle of product types
 * 
 * @author Antony
 */
public class Bundle extends ComparableUsingString implements Serializable {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(Bundle.class);

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   /**
    * The formatter for decimals
    */
   @JsonIgnore
   private NumberFormat currencyFormater = new DecimalFormat("0.00");

   private UUID id;

   /**
    * The name of the product type(s) as they appears in the product catalogue. e.g. Basic Sole Trader
    */
   private String catalogueNames;

   /**
    * The retail price of the product. e.g. 11.99
    */
   private float retailPrice;

   /**
    * The 3 character currency the retail price is expressed. in e.g. GBP
    * 
    * @see https://en.wikipedia.org/wiki/ISO_4217
    */
   private String retailPriceCurrencyCode;

	/**
	 * Whether the commercial product is available for sale (assuming some downloads files available)
	 */
	private Boolean availableForRetailSale;

   /**
    * The product types that define this bundle
    */
   //@Transient
   private Set<ProductType> productTypes = new LinkedHashSet<>();

	/**
	 * No arg constructor see:
	 * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
	 */
	public Bundle(){
	}

   /**
    * A string version including the unique attribute(s) which make up the primary key of this entity
    * 
    * @return A string version of this entity
    */
   @Override
   public String toString() {
      StringBuilder buf = new StringBuilder();
      buf.append("{");
      buf.append(this.getClass().getSimpleName());
      buf.append(":");
      buf.append(this.getCatalogueNames());
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
    * The retail price of the product formatted for the currency. e.g. 11.99
    * 
    * @return the price as a formatted string
    */
   @JsonIgnore
   public String buildRetailPriceFormatted() {
      return this.obtainCurrencyFormater().format(this.getRetailPrice());
   }

   /**
    * The retail price of the product. e.g. 11.99
    * 
    * @return the retailPrice
    */
   public float getRetailPrice() {
      return this.retailPrice;
   }

   /**
    * The retail price of the product. e.g. 11.99
    * 
    * @return the retailPrice
    */
   // public String buildRetailPriceFormatted() {
   // return this.obtainCurrencyFormater().format(this.getRetailPrice());
   // }

   /**
    * The retail price of the product. e.g. 11.99
    * 
    * @param retailPrice
    *           the retailPrice to set
    */
   public void setRetailPrice(final float retailPrice) {
      this.retailPrice = retailPrice;
   }

   /**
    * The currency the retail price is expressed. in e.g. GBP
    * 
    * @return the retailPriceCurrencyCode
    */
   public String getRetailPriceCurrencyCode() {
      return this.retailPriceCurrencyCode;
   }

   /**
    * The currency the retail price is expressed. in e.g. GBP
    * 
    * @param retailPriceCurrencyCode
    *           the retailPriceCurrencyCode to set
    */
   public void setRetailPriceCurrencyCode(final String retailPriceCurrencyCode) {
      this.retailPriceCurrencyCode = retailPriceCurrencyCode;
      Currency currency = Currency.getInstance(this.retailPriceCurrencyCode);
      this.obtainCurrencyFormater().setCurrency(currency);
   }

   /**
    * The product types that define this bundle
    * 
    * @return the productTypes
    */
   public Set<ProductType> getProductTypes() {
      return this.productTypes;
   }

   /**
    * The product types that define this bundle
    * 
    * @param productTypes
    *           the productTypes to set
    */
   public void setProductTypes(final Set<ProductType> productTypes) {

      // Create a set so only unique types are added - the catalogue names is used for uniqueness
      //List<ProductType> productTypeList = new ArrayList<ProductType>();
      //productTypeList.addAll(productTypes);
      // Set<String> catalogueNameSet = new TreeSet<String>();
      // StringBuilder buf = new StringBuilder();
      // for (ProductType productType : productTypes) {
      // String catalogueName = productType.getCatalogueName();
      // if (!catalogueNameSet.contains(catalogueName)) {
      // catalogueNameSet.add(catalogueName);
      // productTypeSet.add(productType);
      // buf.append(catalogueName);
      // }
      // }

      this.productTypes = productTypes;

      StringBuilder buf = new StringBuilder();
      for (ProductType productType : this.getProductTypes()) {
         String catalogueName = productType.getCatalogueName();
         // if (!catalogueNameSet.contains(catalogueName)) {
         // catalogueNameSet.add(catalogueName);
         // productTypeSet.add(productType);
         buf.append(" ");
         buf.append(catalogueName);
         // }
      }
      this.setCatalogueNames(buf.toString());
   }

   /**
    * The name of the product type(s) as they appears in the product catalogue. e.g. Basic Sole Trader
    * 
    * @return the catalogueNames
    */
   public String getCatalogueNames() {
      return this.catalogueNames;
   }

   /**
    * The name of the product type(s) as they appears in the product catalogue. e.g. Basic Sole Trader
    * 
    * @param catalogueNames
    *           the catalogueNames to set
    */
   public void setCatalogueNames(final String catalogueNames) {
      this.catalogueNames = catalogueNames.trim();
   }

	/**
	 * The name of the product type(s) as they appears in the product catalogue. e.g. Basic Sole Trader
	 *
	 * @param availableForRetailSale
	 *           whether or not this bundle should be available for retail sale
	 */
	public void setAvailableForRetailSale(final Boolean availableForRetailSale) {
		this.availableForRetailSale = availableForRetailSale;
	}

	/**
	 * Whether or not this bundle should be available for retail sale
	 *
	 * @return true if this bundle should be available for retail sale, false otherwise
	 */
	public Boolean getAvailableForRetailSale() {
		return this.availableForRetailSale;
	}

	/**
	 * @return the currencyFormater
	 */
	@JsonIgnore
	public NumberFormat obtainCurrencyFormater() {
		return this.currencyFormater;
	}

	/**
    * The formatter for decimals
    * 
    * @param currencyFormater
    *           the currencyFormater to set
    */
   // public void setCurrencyFormater(final NumberFormat currencyFormater) {
   // this.currencyFormater = currencyFormater;
   // }

}