package uk.co.diyaccounting.web.service;

import uk.co.diyaccounting.web.catalogue.*;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.util.regex.Pattern;

/**
 * Top level catalogue services
 * 
 * @author Antony
 */
public interface CatalogueService {

   /**
    * A regular expression that matches a date as used in the accounting period name of the form yyyy-mm-dd. <br/>
    * e.g. 2008-04-05 (Apr08)
    */
   Pattern DATE_PATTERN = Pattern
           .compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

   /**
    * A regular expression that matches the delimiter between products and bundles and between bundles
    */
   Pattern BUNDLE_DELIMITER_PATTERN = Pattern
           .compile("\\" + CatalogueService.BUNDLE_DELIMITER);

   /**
    * A regular expression that matches a 3 character currency code
    */
   Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

   /**
    * The delimiter between products and bundles and between bundles
    */
   String BUNDLE_DELIMITER = "+";

   /**
    * The delimiter between words in a product string (space)
    */
   String WORD_DELIMITER = " ";

   /**
    * The delimiter between items in a product line (comma)
    */
   String ITEM_DELIMITER = ",";

   /**
    * A regular expression that matches an equals
    */
   Pattern EQUALS_PATTERN = Pattern.compile("=");

   /**
    * A regular expression that matches a newline
    */
   Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

   /**
    * A regular expression that matches an accounting period name of the form yyyy-mm-dd (Mmmyy). <br/>
    * e.g. 2008-04-05 (Apr08)
    */
   Pattern ACCOUNTING_PERIOD_PATTERN = Pattern
           .compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [(]{1}[A-Z]{1}[a-z]{2}[0-9]{2}[)]{1}");

   /**
    * The maximum length of a region name is 2 characters, e.g. GB
    */
   int MAX_REGION_NAME_LENGTH = 2;

   void configureCatalogue(String catalogueBundlePricesResource, String catalogueNamesResource,
                           String catalogueResource, String stockPath);

   void logCatalogue();

   void invalidateCatalogue();

   void populateCatalogue(final FileLikePathService file);

   /**
    * Get all the product type names
    * 
    * @param regionName
    *           the region to get the product types for
    * 
    * @return all the product types for the given region
    */
   ProductType[] getProductTypes(String regionName);

   /**
    * Get all the periods for a product component
    * 
    * @param productTypeCatalogueName
    *           the product component without the accounting period specified
    * @param regionName
    *           the region to get the periods for
    * 
    * @return all the periods for a product type and region
    */
   AccountingPeriod[] getAccountingPeriods(String productTypeCatalogueName, String regionName);

   /**
    * Get all the periods for a product component that have a released format available
    * 
    * @param productTypeCatalogueName
    *           the product component without the accounting period specified
    * @param regionName
    *           the region to get the periods for
    * 
    * @return all the periods for a product type and region
    */
   AccountingPeriod[] getAvailableAccountingPeriods(String productTypeCatalogueName, String regionName);

   CommercialProduct buildCommercialProduct(String regionName, String productCatalogueName,
                                            String accountingPeriodName);

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
    * Get the product for a given hash
    * 
    * @param hash
    *           the product component which has the desired hash
    * 
    * @return the product for a given hash
    */
   CommercialProduct getCommercialProductByHash(String hash);

   String getDownloadUrlForCommercialProduct(String baseUrl, String title, String extension)
      throws CatalogueServiceException;

   byte[] getBytesForUrn(String assetUrn) throws CatalogueServiceException;

   /**
    * Get the product for a given type and period
    * 
    * @param productComponent
    *           the product component which is the headline product component
    * @param bundledProductComponents
    *           the bundles (if any) associated with this commercial product
    * 
    * @return the product for a given type and period
    */
   CommercialProduct getCommercialProduct(ProductComponent productComponent,
            ProductComponent[] bundledProductComponents);

   /**
    * Get a list of all entities that relate to the given CommercialProduct
    * 
    * @param commercialProduct
    *           the commercial product to query the periods for
    * 
    * @return a list of all the file formats supported by this commercial product
    */
   AvailableFormat[] getAvailableFormatsForCommercialProduct(CommercialProduct commercialProduct);

   /**
    * Use a commercial products underlying product types obtain the bundle
    * 
    * @return the bundle to which this commercial product belongs
    * 
    * @throws CatalogueServiceException
    *            if the product is not present
    */
   Bundle getBundleForCommercialProducts(CommercialProduct commercialProduct) throws CatalogueServiceException;
}
