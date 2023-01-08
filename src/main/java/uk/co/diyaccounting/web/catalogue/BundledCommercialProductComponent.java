package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;

/**
 * A product that is bundled with another product to become party of a product which can be sold. There may be many bundled components.
 * 
 * @author Antony
 */
public class BundledCommercialProductComponent extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   /**
    * Composite primary key
    */
   public static class Pk implements Serializable{

      private static final long serialVersionUID = 1L;

      private CommercialProduct commercialProduct;
      private ProductComponent productComponent;

      @Override
      public String toString() {
         StringBuilder buf = new StringBuilder();
         buf.append("{");
         buf.append(this.getClass().getSimpleName());
         buf.append(":");
         buf.append(this.getCommercialProduct() != null ? this.getCommercialProduct().toString() : "null");
         buf.append(",");
         buf.append(this.getProductComponent() != null ? this.getProductComponent().toString() : "null");
         buf.append("}");
         return buf.toString();
      }

      public CommercialProduct getCommercialProduct() {
         return commercialProduct;
      }

      public void setCommercialProduct(final CommercialProduct commercialProduct) {
         this.commercialProduct = commercialProduct;
      }

      public ProductComponent getProductComponent() {
         return productComponent;
      }

      public void setProductComponent(final ProductComponent productComponent) {
         this.productComponent = productComponent;
      }
   }

   /**
    * The query for obtaining the commercial products that have the same headline product component
    */
   public static final String FIND_COMMERCIAL_PRODUCTS = "findCommercialProducts";

   //@Id
   //@GeneratedValue(generator = "uuid")
   //@GenericGenerator(name = "uuid", strategy = "uuid2")
   //@Column(name = "id")
   //private UUID id;

   /**
    * The product that this component will be part of
    */
   private CommercialProduct commercialProduct;

   /**
    * The product component
    */
   private ProductComponent productComponent;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public BundledCommercialProductComponent(){
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
      buf.append(this.getCommercialProduct() != null ? this.getCommercialProduct().toString() : "null");
      buf.append(",");
      buf.append(this.getProductComponent() != null ? this.getProductComponent().toString() : "null");
      buf.append("}");
      return buf.toString();
   }

   /**
    * The primary key of the entity
    * 
    * @return the id
    */
   //public UUID getId() {
   //   return this.id;
   //}

   /**
    * The primary key of the entity
    * 
    * @param id
    *           the id to set
    */
   //public void setId(final UUID id) {
   //   this.id = id;
   //}

   /**
    * The product that this component will be part of
    * 
    * @return the commercialProduct
    */
   public CommercialProduct getCommercialProduct() {
      return this.commercialProduct;
   }

   /**
    * The product that this component will be part of
    * 
    * @param commercialProduct
    *           the commercialProduct to set
    */
   public void setCommercialProduct(final CommercialProduct commercialProduct) {
      this.commercialProduct = commercialProduct;
   }

   /**
    * The product component
    * 
    * @return the productComponent
    */
   public ProductComponent getProductComponent() {
      return this.productComponent;
   }

   /**
    * The product component
    * 
    * @param productComponent
    *           the productComponent to set
    */
   public void setProductComponent(final ProductComponent productComponent) {
      this.productComponent = productComponent;
   }

}
