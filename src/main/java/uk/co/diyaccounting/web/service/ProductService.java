package uk.co.diyaccounting.web.service;

import uk.co.diyaccounting.web.catalogue.*;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Product;

/**
 * Content operations for product content
 * 
 * @author Antony
 */
public interface ProductService {


	void logCatalogue();
	void invalidateCatalogue();

	void populateCatalogue();

   /**
    * Return all the products with their content objects populated
    * 
    * @return all the product content
    * 
    */
   Product[] getAllProducts();

   /**
    * Return all the products with their content objects populated
    * This is used for site navigation where, the product is pure content such as Business Insurance.
    * 
    * @return all the product content
    * 
    */
   Product[] getAllProductsIncludingThoseNotInCatalogue();

   /**
    * Return all the products that do not have bundles
    * 
    * @return the base product content
    * 
    */
   ProductType[] getBaseProducts();

   /**
    * Return the named products with content populated
    * 
    * @param name
    *           the name of the product to get
    * @return the product content
    * 
    */
   Product getProduct(String name);

   /**
    * Does the named product type exist in the catalogue
    * 
    * @param name
    *           the name of the product to check
    * @return true of the product is in the catalogue
    */
   boolean hasProductType(String name);

   /**
    * Return the named product type from the catalogue
    * 
    * @param name
    *           the name of the product to get
    * @return the product content
    * 
    */
   ProductType getProductType(String name);

   /**
    * Get all the periods for a product component
    * 
    * @param productType
    *           the product to get the periods for
    * 
    * @return all the periods for a product type and region
    * 
    */
   AccountingPeriod[] getAccountingPeriods(ProductType productType);

	AccountingPeriod[] getAccountingPeriodsForProductName(final String catalogueName);

   /**
    * Get the named accounting period
    * 
    * @param productType
    *           the product to get the periods for
    * @param periodName
    *           the period to get
    * 
    * @return the named period
    * 
    */
   AccountingPeriod getAccountingPeriod(ProductType productType, String periodName);

   /**
    * Get all the possible bundles for a commercial product
    * 
    * @param region
    *           the region
    * @param productType
    *           the product component without the accounting period specified
    * @param accountingPeriod
    *           the accounting period for this product type
    * 
    * @return all the product component sets for a given headline product component
    */
   ProductComponent buildProductComponent(Region region, ProductType productType, AccountingPeriod accountingPeriod);

   /**
    * Get all the possible bundles for a commercial product
    * 
    * @param productComponent
    *           the product component to check for bundle options
    * 
    * @return all the product component sets for a given headline product component
    */
   ProductComponent[][] getBundledProductComponents(ProductComponent productComponent);

   /**
    * Get the product by hashed identifier
    * 
    * @param hash
    *           a unique hashed id for the product
    * 
    * @return the product for a given type and period
    * 
    */
   CommercialProduct getCommercialProductByHash(String hash);

	String getDownloadUrlForCommercialProduct(String baseUrl, CommercialProduct commercialProduct, AvailableFormat availableFormat, String extension)
			throws CatalogueServiceException;

	CommercialProduct getCommercialProduct(String regionName, String productCatalogueName, String accountingPeriodName);

	byte[] getBytesForUrn(String assetUrn) throws CatalogueServiceException;

   /**
    * Get the product for a given type and period
    * 
    * @param region
    *           the region
    * @param productComponent
    *           the product component which is the headline product component
    * @param bundleNamePeriods
    *           the bundles (if any) associated with this commercial product
    * 
    * @return the product for a given type and period
    * 
    */
   CommercialProduct getCommercialProduct(Region region, ProductComponent productComponent,
            String[] bundleNamePeriods);

   /**
    * Return the feature content
    * 
    * @param name
    *           the name of the product to get
    * 
    * @return the feature content
    * 
    */
   Feature getFeature(String name);

   /**
    * Return the named features
    * 
    * @param featureNames
    *           the featureNames of the features to get
    * @return the feature content
    * 
    */
   Feature[] getFeatures(String[] featureNames);

   /**
    * Return the named products
    * 
    * @param productNames
    *           the productNames of the products to get
    * @return the product content
    * 
    */
   Product[] getProducts(String[] productNames);

	/**
	 * Set the product content base path
	 *
	 * @param productContentBasePath
	 *           the productContentBasePath to set
	 */
	void setProductContentBasePath(final String productContentBasePath);

	/**
	 * The product content base path
	 *
	 * @return productContentBasePath
	 */
	String getProductContentBasePath();

	/**
	 * The name of classpath resource containing catalogue data
	 *
	 * @return catalogueResource
	 */
	String getCatalogueResource();

	/**
	 * The name of classpath resource containing catalogue data
	 *
	 * @param catalogueResource
	 */
	void setCatalogueResource(final String catalogueResource);

	/**
	 * The name of classpath resource containing catalogue data
	 *
	 * @return catalogueBundlePriceResource
	 */
	String getCatalogueBundlePriceResource();

	/**
	 * The name of classpath resource containing catalogue data
	 *
	 * @param catalogueBundlePriceResource
	 */
	void setCatalogueBundlePriceResource(final String catalogueBundlePriceResource);

	/**
	 * The name of classpath resource containing catalogue data
	 *
	 * @return catalogueNamesResource
	 */
	String getCatalogueNamesResource();

	/**
	 * The name of classpath resource containing catalogue data
	 *
	 * @param catalogueNamesResource
	 */
	void setCatalogueNamesResource(final String catalogueNamesResource);

	/**
    * Build the product description from a commercial product
    * 
    * @param commercialProduct
    *           the product to build the description from
    * 
    * @return the description as a string
    */
   String getDescription(CommercialProduct commercialProduct);

	/**
	 * Get the available formats for a product
	 *
	 * @param commercialProduct
	 *           the product to get the available formats for
	 *
	 * @return the available formats for this commercial product
	 */
	AvailableFormat[] getAvailableFormats(CommercialProduct commercialProduct);
}
