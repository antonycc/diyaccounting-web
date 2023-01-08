package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;
import java.util.UUID;

/**
 * A type of product which would typically map to many commercial products for each covered period
 * 
 * @author Antony
 */
public class ProductType extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   private UUID id;

   /**
    * The name of the product type as it appears in the product catalogue. e.g. Basic Sole Trader
    */
   private String catalogueName;

   /**
    * The name of the product as it appears in the content management system. e.g. BasicSoleTraderProduct
    */
   private String contentItemName;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public ProductType(){
   }

   /**
    * The function this product type is designed for
    */
   private PrimaryFunction primaryFunction;

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
      buf.append(this.getCatalogueName());
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
    * The name of the product type as it appears in the product catalogue. e.g. Basic Sole Trader
    * 
    * @return the catalogueName
    */
   public String getCatalogueName() {
      return this.catalogueName;
   }

   /**
    * The name of the product type as it appears in the product catalogue. e.g. Basic Sole Trader
    * 
    * @param catalogueName
    *           the catalogueName to set
    */
   public void setCatalogueName(final String catalogueName) {
      this.catalogueName = catalogueName;
   }

   /**
    * The name of the product as it appears in the content management system. e.g. BasicSoleTraderProduct
    * 
    * @return the contentItemName
    */
   public String getContentItemName() {
      return this.contentItemName;
   }

   /**
    * The name of the product as it appears in the content management system. e.g. BasicSoleTraderProduct
    * 
    * @param contentItemName
    *           the contentItemName to set
    */
   public void setContentItemName(final String contentItemName) {
      this.contentItemName = contentItemName;
   }

   /**
    * The function this product type is designed for
    * 
    * @return the primaryFunction
    */
   public PrimaryFunction getPrimaryFunction() {
      return this.primaryFunction;
   }

   /**
    * The function this product type is designed for
    * 
    * @param primaryFunction
    *           the primaryFunction to set
    */
   public void setPrimaryFunction(final PrimaryFunction primaryFunction) {
      this.primaryFunction = primaryFunction;
   }

}