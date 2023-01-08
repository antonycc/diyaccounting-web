package uk.co.diyaccounting.web.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.co.diyaccounting.web.catalogue.*;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.io.UtilConstants;
import uk.co.polycode.mdcms.util.net.UrlHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("catalogue")
public class CatalogueServiceMemImpl implements CatalogueService {

   private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceMemImpl.class);

   /**
    * A regular expression that matches a 3 character currency code
    */
   public static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]{3}");

   /**
    * A regular expression that matches a date as used in the accounting period name of the form yyyy-mm-dd. <br/>
    * e.g. 2008-04-05 (Apr08)
    */
   private static final Pattern DATE_PATTERN = Pattern
           .compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

   private UrlHelper url = new UrlHelper();

   private String catalogueResource = null;
   private String catalogueNamesResource = null;
   private String catalogueBundlePriceResource = null;
   private FileLikePathService file = null;
   private Map<String, CatalogueItem> catalogue = null;
   private String stockPath = null;

   @Override
   public synchronized void configureCatalogue(
         final String catalogueBundlePricesResource,
         final String catalogueNamesResource,
         final String catalogueResource,
         final String stockPath) {
      this.catalogueResource = catalogueResource;
      this.catalogueNamesResource = catalogueNamesResource;
      this.catalogueBundlePriceResource = catalogueBundlePricesResource;
      this.stockPath = stockPath;
   }

   @Override
   public void logCatalogue() {
      logger.info("Catalogue");
      for(CatalogueItem item : this.catalogue.values()){
         AvailableFormat[] availableFormats = item.toAvailableFormats();
         String catalogueName;
         if(ArrayUtils.isEmpty(availableFormats)){
            catalogueName = item.productTypeCatalogueName;
            logger.warn("Could not find available format for product type catalogue name: [{}]", catalogueName);
         }else{
            catalogueName = availableFormats[0].getCommercialProduct().getBundle().getCatalogueNames();
         }
         logger.info("{}: {} {} {}{}",
               item.availabilityStatusName,
               catalogueName,
               String.join(" ", item.fileFormatNames),
               item.retailPrice,
               item.retailPriceCurrencyCode);
      }
      logger.info("Catalogue contains {} items", this.catalogue.size());
   }

   @Override
   public synchronized void invalidateCatalogue() {
      logger.info("Invalidating Catalogue");
      this.catalogue = new HashMap<>();
   }

   /**
    * Parse catalogueSource expecting:
    *
    * Package,Platform,Status,Legacy
    * GB Accounts Basic Sole Trader 2017-04-05 (Apr17),Excel 2003,released,unused
    */
   @Override
   public synchronized void populateCatalogue(final FileLikePathService file) {
      this.file = file;
      this.invalidateCatalogue();
      logger.info("Loading in memory catalogue.");
      try {
         InputStream cis = this.file.getInputStreamForUrn(this.catalogueResource);
         String catalogueSource = IOUtils.toString(cis, UtilConstants.DEFAULT_ENCODING);
         InputStream cnis = this.file.getInputStreamForUrn(this.catalogueNamesResource);
         String catalogueNamesSource = IOUtils.toString(cnis, UtilConstants.DEFAULT_ENCODING);
         InputStream cbis = this.file.getInputStreamForUrn(this.catalogueBundlePriceResource);
         String catalogueBundlePricesSource = IOUtils.toString(cbis, UtilConstants.DEFAULT_ENCODING);
         Properties prices = new Properties();
         prices.load(new StringReader(catalogueBundlePricesSource));
         Properties names = new Properties();
         names.load(new StringReader(catalogueNamesSource));
         String[] catalogueLines = NEWLINE_PATTERN.split(catalogueSource);
         for (String line : catalogueLines) {
            if(line.startsWith("Package")){
               continue;
            }
            StringTokenizer catalogueLineToken = new StringTokenizer(line, ITEM_DELIMITER);
            String commercialProductName = catalogueLineToken.nextToken();
            try {
               String fileFormatName = catalogueLineToken.nextToken();
               String availabilityStatusName = catalogueLineToken.nextToken();
               CatalogueItem catalogueItem = this.catalogue.get(commercialProductName);
               if (catalogueItem == null) {
                  StringTokenizer commercialProductNameToken = new StringTokenizer(commercialProductName, WORD_DELIMITER);
                  catalogueItem = new CatalogueItem();
                  catalogueItem.regionName = commercialProductNameToken.nextToken();
                  catalogueItem.primaryFunctionName = commercialProductNameToken.nextToken();
                  ArrayList<String> namePeriod = new ArrayList<>();
                  while (commercialProductNameToken.hasMoreTokens()){
                     namePeriod.add(commercialProductNameToken.nextToken());
                  }
                  if(namePeriod.size() < 3){
                     logger.warn("Expected at least 3 words after {}, found {}} in {}", catalogueItem.primaryFunctionName, namePeriod.size(), line);
                     continue;
                  }
                  StringBuilder productTypeCatalogueName = new StringBuilder();
                  for(int i=0; i<namePeriod.size()-2; i++){
                     productTypeCatalogueName.append(namePeriod.get(i));
                     productTypeCatalogueName.append(WORD_DELIMITER);
                  }
                  catalogueItem.productTypeCatalogueName = productTypeCatalogueName.toString().trim();
                  StringBuilder accountingPeriodName = new StringBuilder();
                  accountingPeriodName.append(namePeriod.get(namePeriod.size()-2));
                  accountingPeriodName.append(WORD_DELIMITER);
                  accountingPeriodName.append(namePeriod.get(namePeriod.size()-1));
                  catalogueItem.accountingPeriodName = accountingPeriodName.toString();
               }
               catalogueItem.fileFormatNames.add(fileFormatName);
               catalogueItem.availabilityStatusName = availabilityStatusName;

               // Map to content item
               String urlEncodedProductTypeCatalogueName = this.url.encodeQueryParam(catalogueItem.productTypeCatalogueName);
               catalogueItem.contentItemName = names.getProperty(urlEncodedProductTypeCatalogueName);
               if(StringUtils.isBlank(catalogueItem.contentItemName)) {
                  catalogueItem.contentItemName = names.getProperty(catalogueItem.productTypeCatalogueName);
               }
               if(StringUtils.isBlank(catalogueItem.contentItemName)) {
                     logger.warn("Could not map catalogue name [{}] to content item", catalogueItem.productTypeCatalogueName);
               }

               // Map to catalogue price
               StringBuffer regionPrimaryFunctionProductTypeCatalogueName = new StringBuffer();
               regionPrimaryFunctionProductTypeCatalogueName.append(catalogueItem.regionName);
               regionPrimaryFunctionProductTypeCatalogueName.append(WORD_DELIMITER);
               regionPrimaryFunctionProductTypeCatalogueName.append(catalogueItem.primaryFunctionName);
               regionPrimaryFunctionProductTypeCatalogueName.append(WORD_DELIMITER);
               regionPrimaryFunctionProductTypeCatalogueName.append(catalogueItem.productTypeCatalogueName);
               String urlEncodedRegionPrimaryProductTypeCatalogueName = this.url.encodeQueryParam(
                     regionPrimaryFunctionProductTypeCatalogueName.toString());
               String retailPriceString = prices.getProperty(urlEncodedRegionPrimaryProductTypeCatalogueName);
               if(StringUtils.isBlank(retailPriceString)) {
                  retailPriceString = names.getProperty(regionPrimaryFunctionProductTypeCatalogueName.toString());
               }
               if(StringUtils.isBlank(retailPriceString)) {
                  logger.warn("Could not map catalogue name [{}] to price", catalogueItem.productTypeCatalogueName);
               }else{
                  Matcher currencyMatcher = CatalogueServiceMemImpl.CURRENCY_PATTERN.matcher(retailPriceString);
                  currencyMatcher.find();
                  if(currencyMatcher.start() >= 0){
                     catalogueItem.retailPrice = Float.parseFloat(retailPriceString.substring(0, currencyMatcher.start()));
                     catalogueItem.retailPriceCurrencyCode = retailPriceString.substring(currencyMatcher.start());
                  }else{
                     logger.warn("Could not locate price currency pair in [{}] ", retailPriceString);
                  }
               }

               logger.info("{}: {} {} {}", availabilityStatusName, commercialProductName, fileFormatName, retailPriceString);
               this.catalogue.put(commercialProductName, catalogueItem);
            }catch (Exception e){
               logger.warn("Could not parse catalogue line: [{}]", line);
            }
         }
         logger.info("Loaded {} catalogue items", this.catalogue.size());
      }catch (IOException e){
         logger.error("Could not load catalogue item", e);
         logger.error("Catalogue:");
         logger.error(this.catalogueResource);
         logger.error("Name mappings:");
         logger.error(this.catalogueNamesResource);
         logger.error("Prices:");
         logger.error(this.catalogueBundlePriceResource);
      }
   }

   private void initCatalogue(){
      if(this.catalogue == null){
         if(this.catalogueResource == null) {
            logger.error("Cannot initialise catalogue while catalogueResource is null, first call configureCatalogue.");
         }else if(this.file == null){
            logger.warn("Initialise catalogue with default File support because populateCatalogue has not been called.");
            this.populateCatalogue(new FileLikePathService());
         }else {
            this.populateCatalogue(this.file);
         }
      }
   }

   @Override
   public ProductType[] getProductTypes(final String regionName) {
      this.initCatalogue();
      return this.catalogue.values().stream()
            .filter(catalogueItem -> regionName.equals(catalogueItem.regionName))
            .map(catalogueItem -> catalogueItem.toProductType())
            .collect(Collectors.toList())
            .toArray(new ProductType[0]);
   }

   @Override
   public AccountingPeriod[] getAccountingPeriods(final String productTypeCatalogueName, final String regionName) {
      this.initCatalogue();
      return this.catalogue.values().stream()
            .filter(catalogueItem -> regionName.equals(catalogueItem.regionName))
            .filter(catalogueItem -> productTypeCatalogueName.equals(catalogueItem.productTypeCatalogueName))
            .map(catalogueItem -> catalogueItem.toAccountingPeriod())
            .collect(Collectors.toList())
            .toArray(new AccountingPeriod[0]);
   }

   @Override
   public AccountingPeriod[] getAvailableAccountingPeriods(final String productTypeCatalogueName, final String regionName) {
      this.initCatalogue();
      return this.catalogue.values().stream()
            .filter(catalogueItem -> regionName.equals(catalogueItem.regionName))
            .filter(catalogueItem -> productTypeCatalogueName.equals(catalogueItem.productTypeCatalogueName))
            .filter(catalogueItem -> "released".equals(catalogueItem.availabilityStatusName))
            .map(catalogueItem -> catalogueItem.toAccountingPeriod())
            .collect(Collectors.toList())
            .toArray(new AccountingPeriod[0]);
   }

   // TODO Remove the bundledProductComponents parameter
   @Override
   public CommercialProduct getCommercialProduct(final ProductComponent productComponent, final ProductComponent[] bpc) {
      this.initCatalogue();
      return this.catalogue.values().stream()
            .filter(catalogueItem -> productComponent.getRegion().getName().equals(catalogueItem.regionName))
            .filter(catalogueItem -> productComponent.getProductType().getCatalogueName().equals(catalogueItem.productTypeCatalogueName))
            .filter(catalogueItem -> productComponent.getAccountingPeriod().getName().equals(catalogueItem.accountingPeriodName))
            .filter(catalogueItem -> "released".equals(catalogueItem.availabilityStatusName))
            .map(catalogueItem -> catalogueItem.toCommercialProduct())
            .collect(Collectors.toList())
            .get(0);
   }

   @Override
   public CommercialProduct getCommercialProductByHash(final String hash) {
      this.initCatalogue();
      return this.catalogue.values().stream()
            .map(catalogueItem -> catalogueItem.toCommercialProduct())
            .filter(commercialProduct -> hash.equals(commercialProduct.getHash()))
            .collect(Collectors.toList())
            .get(0);
   }

   @Override
   public String getDownloadUrlForCommercialProduct(final String baseUrl, final String title, String extension)
         throws CatalogueServiceException{
      String key = title + extension;
      String resourcePath = this.stockPath + key;
      try {
         return this.file.getDownloadUrlForAssetAtUrn(baseUrl, this.stockPath, resourcePath);
      }catch (IOException e){
         throw new CatalogueServiceException(resourcePath, e);
      }
   }

   @Override
   public byte[] getBytesForUrn(final String assetUrn) throws CatalogueServiceException{
      try {
         InputStream is = this.file.getInputStreamForUrn(assetUrn);
         return is.readAllBytes();
      }catch (IOException e){
         throw new CatalogueServiceException(assetUrn, e);
      }
   }

   @Override
   public AvailableFormat[] getAvailableFormatsForCommercialProduct(final CommercialProduct commercialProduct) {
      this.initCatalogue();
      return this.catalogue.values().stream()
            .filter(catalogueItem -> commercialProduct.getProductComponent().getRegion().getName().equals(catalogueItem.regionName))
            .filter(catalogueItem -> commercialProduct.getProductComponent().getProductType().getCatalogueName().equals(catalogueItem.productTypeCatalogueName))
            .filter(catalogueItem -> commercialProduct.getProductComponent().getAccountingPeriod().getName().equals(catalogueItem.accountingPeriodName))
            .filter(catalogueItem -> "released".equals(catalogueItem.availabilityStatusName))
            .collect(Collectors.toList())
            .get(0).toAvailableFormats();
   }

   @Override
   public Bundle getBundleForCommercialProducts(final CommercialProduct commercialProduct) throws CatalogueServiceException {
      if(true){
         throw new NotImplementedException("getBundleForCommercialProducts is not implemented, do we need it?");
      }
      return null;
   }

   @Override
   public ProductComponent[][] getBundledProductComponents(final ProductComponent productComponent) {
      if(true){
         throw new NotImplementedException("getBundledProductComponent is not implemented, do we need it?");
      }
      return new ProductComponent[0][0];
   }

   @Override
   public CommercialProduct buildCommercialProduct(final String regionName, final String productCatalogueName, final String accountingPeriodName) {
      this.initCatalogue();
      //CatalogueItem catalogueItem = new CatalogueItem();
      //catalogueItem.regionName = regionName;
      //catalogueItem.productTypeCatalogueName = productCatalogueName;
      //catalogueItem.accountingPeriodName = accountingPeriodName;
      //CommercialProduct commercialProduct = catalogueItem.toCommercialProduct();
      //return commercialProduct;
      return this.catalogue.values().stream()
            .filter(catalogueItem -> regionName.equals(catalogueItem.regionName))
            .filter(catalogueItem -> productCatalogueName.equals(catalogueItem.productTypeCatalogueName))
            .filter(catalogueItem -> accountingPeriodName.equals(catalogueItem.accountingPeriodName))
            .filter(catalogueItem -> "released".equals(catalogueItem.availabilityStatusName))
            .map(catalogueItem -> catalogueItem.toCommercialProduct())
            .collect(Collectors.toList())
            .get(0);

   }
}
