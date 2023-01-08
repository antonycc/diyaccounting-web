package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;

/**
 * A product that is bundled with another product to become party of a product which can be sold. There may only be 1
 * component.
 * 
 * @author Antony
 */
public class BundleProductType extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   /**
    * Composite primary key
    */
   //public static class Pk extends UniqueQuery implements Serializable{

   //   @Transient
   //   private static final long serialVersionUID = 1L;

   //   private Bundle bundle;
   //   private ProductType productType;

   //   @Override
   //   public void setUniqueParameters(final Query query) {
   //      query.setParameter("bundle", this.getBundle());
   //      query.setParameter("productType", this.getProductType());
   //   }

   //   @Override
   //   public void copyAttributesForUpdate(final ComparableUsingString o) {
   //      Pk entity = (Pk)o;
   //      this.setBundle(entity.getBundle());
   //      this.setProductType(entity.getProductType());
         /*
         if(entity.getBundle() != null) {
            Bundle bundleCopy = new Bundle();
            bundleCopy.copyAttributesForUpdate(entity.getBundle());
            this.setBundle(bundleCopy);
         }
         if(entity.getProductType() != null) {
            ProductType productTypeCopy = new ProductType();
            productTypeCopy.copyAttributesForUpdate(entity.getProductType());
            this.setProductType(productTypeCopy);
         }
         */
   //   }

   //   @Override
   //   public String toString() {
   //      StringBuilder buf = new StringBuilder();
   //      buf.append("{");
   //      buf.append(this.getClass().getSimpleName());
   //      buf.append(":");
   //      buf.append(this.getBundle() != null ? this.getBundle().toString() : "null");
   //      buf.append(",");
   //      buf.append(this.getProductType() != null ? this.getProductType().toString() : "null");
   //      buf.append("}");
   //      return buf.toString();
   //   }

   //   public Bundle getBundle() {
   //      return bundle;
   //   }

   //   public void setBundle(final Bundle bundle) {
   //      this.bundle = bundle;
   //   }

   //   public ProductType getProductType() {
   //      return productType;
   //   }

   //   public void setProductType(final ProductType productType) {
   //      this.productType = productType;
   //   }
   //}

   //@Id
   //@GeneratedValue(generator = "uuid")
   //@GenericGenerator(name = "uuid", strategy = "uuid2")
   //@Column(name = "id")
   //private UUID id;

   /**
    * The bundle that this product type will be part of
    */
   private Bundle bundle;

   /**
    * The product type
    */
   private ProductType productType;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public BundleProductType(){
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
      buf.append(this.getBundle() != null ? this.getBundle().toString() : "null");
      buf.append(",");
      buf.append(this.getProductType() != null ? this.getProductType().toString() : "null");
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
    * The bundle that this product type will be part of
    * 
    * @return the bundle
    */
   public Bundle getBundle() {
      return this.bundle;
   }

   /**
    * The bundle that this product type will be part of
    * 
    * @param bundle
    *           the bundle to set
    */
   public void setBundle(final Bundle bundle) {
      this.bundle = bundle;
   }

   /**
    * The product type
    * 
    * @return the productType
    */
   public ProductType getProductType() {
      return this.productType;
   }

   /**
    * The product type
    * 
    * @param productType
    *           the productType to set
    */
   public void setProductType(final ProductType productType) {
      this.productType = productType;
   }

}