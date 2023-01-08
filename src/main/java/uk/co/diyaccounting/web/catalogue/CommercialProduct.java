package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;
import uk.co.polycode.mdcms.util.security.HashHelper;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * A product that can be purchased by an end user.
 *
 * @author Antony
 */
public class CommercialProduct extends ComparableUsingString implements Serializable {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CommercialProduct.class);

   @JsonIgnore
   private static final long serialVersionUID = 1L;

	@JsonIgnore
	private transient HashHelper hashHelper = new HashHelper();

   /**
    * The query for obtaining the commercial products that have the same headline product component
    */
   @JsonIgnore
   public static final String FIND_COMMERCIAL_PRODUCTS_FOR_PRODUCT_COMPONENT = "findCommercialProductsForProductComponent";

   private UUID id;


   /**
    * A hash of the products unique attributes
    */
   private String hash;

   /**
    * The headline type for this product
    */
   // @Transient
   private ProductComponent productComponent = null;

   /**
    * The bundled components (if any) for this product
    */
   private final Set<ProductComponent> bundledProductComponents = new LinkedHashSet<>();

   /**
    * The bundle this commercial product belongs to
    */
   private Bundle bundle = null;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public CommercialProduct(){
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
      // Headline product component
      buf.append(this.getProductComponent() != null ? this.getProductComponent().toString() : "null");
      buf.append(",");
      // Bundled components
      for (ProductComponent productComponent : this.getBundledProductComponents()) {
         buf.append("+");
         buf.append(productComponent.toString());
      }
      buf.append("}");
      return buf.toString();
   }

   /**
    * Obtain all the product types related to this commercial product
    *
    * @return a list of product types from the constituent parts
    */
   public Set<ProductType> buildSetOfProductTypes() {
      Set<ProductType> productTypes = new TreeSet<ProductType>();
      productTypes.add(this.getProductComponent().getProductType());
      for (ProductComponent productComponent : this.getBundledProductComponents()) {
         productTypes.add(productComponent.getProductType());
      }
      return productTypes;
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
    * The headline type for this product
    *
    * @return the productComponent
    */
   public ProductComponent getProductComponent() {
      return this.productComponent;
   }

   /**
    * The headline type for this product
    *
    * @param productComponent
    *           the productComponent to set
    */
   public void setProductComponent(final ProductComponent productComponent) {
      this.productComponent = productComponent;
      this.updateHash();
   }

   /**
    * The bundled components (if any) for this product
    *
    * @return the bundledProductComponents
    */
   public Set<ProductComponent> getBundledProductComponents() {
      return this.bundledProductComponents;
   }

   /**
    * The bundled components (if any) for this product
    *
    * @param bundledProductComponents
    *           the bundledProductComponents to set
    */
   // public void setBundledProductComponents(final List<ProductComponent> bundledProductComponents) {
   // this.bundledProductComponents = bundledProductComponents;
   // this.updateHash();
   // }

   /**
    * Add a bundled component for this product
    *
    * @param bundledProductComponent
    *           the bundledProductComponent to set
    */
   public void addBundledProductComponent(final ProductComponent bundledProductComponent) {
      this.bundledProductComponents.add(bundledProductComponent);
      this.updateHash();
   }

   /**
    * The number of bundled components (if any) for this product
    *
    * @return the number of bundledProductComponents
    */
   public int countBundledProductComponents() {
      return this.bundledProductComponents.size();
   }

   /**
    * A hash of the products unique attributes
    *
    * @return the hash
    */
   public String getHash() {
      // if(this.productComponent != null){
      // this.hash = this.toString();
      // }
      // this.hash = this.hashHelper.getHash(this.toString());
      // logger.debug("Commercial product hash is: {}", this.hash);
      // logger.debug("Headline product is: {}", this.getProductComponent() != null ?
      // this.getProductComponent().toString() : "null");
      // logger.debug("Number of bundles is: {}", this.bundledProductComponents.size());
      // logger.debug("As a string it's: {}", this.toString());
      return this.hash;
   }

   /**
    * A hash of the products unique attributes
    *
    * @param hash
    *           the hash to set
    */
   public void setHash(final String hash) {
      this.hash = hash;
   }

   /**
    * The bundle this commercial product belongs to
    *
    * @return the bundle
    */
   public Bundle getBundle() {
      return this.bundle;
   }

   /**
    * The bundle this commercial product belongs to
    *
    * @param bundle
    *           the bundle to set
    */
   public void setBundle(final Bundle bundle) {
      this.bundle = bundle;
   }

   /**
    * Calculate a new hash and set it to the hash attribute
    */
   public void updateHash() {
      this.hash = this.calculateHash(this.toString());
   }

   /**
    * Calculate a new hash and set it to the hash attribute
    */
   public String calculateHash(final String stringToHash) {
      return this.hashHelper.getHash(stringToHash);
      // return stringToHash;
   }

   public String calculateProductTitle(final AvailableFormat availableFormat) {
      ProductComponent productComponent = this.getProductComponent();
      String regionName = productComponent.getRegion().getName();
      String primaryFunctionName = productComponent.getProductType().getPrimaryFunction().getName();
      String productTypeName = productComponent.getProductType().getCatalogueName();
      String periodName = productComponent.getAccountingPeriod().getName();
      String fileFormatName = availableFormat.getFileFormat().getName();
      String title = regionName + " " +
            primaryFunctionName + " " +
            productTypeName + " " +
            periodName + " " +
            fileFormatName;
      return title;
   }
}