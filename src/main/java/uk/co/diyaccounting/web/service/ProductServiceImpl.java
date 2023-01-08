package uk.co.diyaccounting.web.service;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.diyaccounting.web.catalogue.*;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Product;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.net.UrlHelper;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Accesses product level content
 */
@Service("productContent")
public class ProductServiceImpl implements ProductService {

   private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

   public static final Pattern AMPERSAND_PATTERN = Pattern.compile("&");

   public static final String REGION_NAME = "GB";

   @Autowired
   @Qualifier("content")
   private ContentService content;

   @Autowired
   @Qualifier("catalogue")
   private CatalogueService catalogue;

   @Autowired
   @Qualifier("fileLikePaths")
   private FileLikePathService fileLikePathService;

   protected UrlHelper url = new UrlHelper();

   private Product product = new Product();

   private Feature feature = new Feature();

   @Value("${catalogue.ingestCatalogue}")
   private boolean ingestCatalogue;

   @Value("${catalogue.stockPath}")
   private String stockPath;

   /**
    * The base path for product content
    * e.g. "/content/"
    */
   @Value("${content.productContentBasePath}")
   private String productContentBasePath;

   /**
    * The name of classpath resource containing catalogue data
    */
   @Value("${catalogue.catalogueResource}")
   private String catalogueResource;

   /**
    * The name of classpath resource containing catalogue data
    */
   @Value("${catalogue.catalogueBundlePriceResource}")
   private String catalogueBundlePriceResource;

   /**
    * The name of classpath resource containing catalogue data
    */
   @Value("${catalogue.catalogueNamesResource}")
   private String catalogueNamesResource;

   /**
    * Load the catalogue into memory for the product service
    */
   @PostConstruct
   public void init() throws IOException, ParseException {
      logger.info("ingestCatalogue: {}", this.ingestCatalogue);
      if (this.ingestCatalogue) {
         this.populateCatalogue();
      }
   }

   @Override
   public void logCatalogue() {

      try {
         this.catalogue.logCatalogue();
         logger.info("Catalogue loaded");
      } catch (Exception e) {
         //e.printStackTrace();
         throw new ContentException("Failed to invalidate product catalogue", this.catalogueResource, e);
      }
   }

   @Override
   public void invalidateCatalogue() {

      // Load the catalogue if it isn't already loaded
      try {
         logger.info("Invalidating the catalogue from: {}", this.catalogueResource);
         this.catalogue.invalidateCatalogue();
         logger.info("Catalogue loaded");
      } catch (Exception e) {
         //e.printStackTrace();
         throw new ContentException("Failed to invalidate product catalogue", this.catalogueResource, e);
      }
   }

   @Override
   public void populateCatalogue() {

      // Load the catalogue if it isn't already loaded
      try {
         logger.info("Loading catalogue from: {}", this.catalogueResource);
         this.catalogue.configureCatalogue(
               this.catalogueBundlePriceResource,
               this.catalogueNamesResource,
               this.catalogueResource,
               this.stockPath);
         this.catalogue.populateCatalogue(this.fileLikePathService);
         logger.info("Catalogue loaded");
      } catch (Exception e) {
	      //e.printStackTrace();
         throw new ContentException("Failed to load product catalogue", this.catalogueResource, e);
      }
   }

   @Override
   public ProductType[] getBaseProducts() {

      // Start with the full set of product types and the set of content items
      ProductType[] allTypes = this.catalogue.getProductTypes(ProductServiceImpl.REGION_NAME);
      Product[] allContent = this.getAllProducts();

      // De-duplicate
      //ProductType[] uniqueTypes = (ProductType[])(new LinkedHashSet<>(Arrays.asList(allTypes)).toArray());
      LinkedHashSet<ProductType> uniqueTypes = new LinkedHashSet<>(Arrays.asList(allTypes));

      // Filter out product types we don't have content for to get the intersection
      Vector<ProductType> populatedTypes = new Vector<ProductType>();
      for (ProductType candidate : uniqueTypes) {
         for (Product ci : allContent) {
            if (ci.getName().equals(candidate.getContentItemName())) {
               populatedTypes.add(candidate);
               break;
            }
         }
      }

      return populatedTypes.toArray(new ProductType[populatedTypes.size()]);
   }

   @Override
   public Product[] getAllProducts() {

      // Obtain the list of product from the CMS
      //ContentList products = this.content.getContentList(ProductServiceImpl.productsContentItemPath);
      //String[] productContentItemNames = products.getContentList();
      String[] productContentItemNames = getMasterProductList();

      // Obtain the list of products from the Catalogue
      ProductType[] productTypes = this.catalogue.getProductTypes(ProductServiceImpl.REGION_NAME);

      // Build an array of populated products
      ArrayList<Product> productVector = new ArrayList<Product>();
      for (String productContentName : productContentItemNames) {

         // Populate the products with data from the CMS
         Product populatedProduct;
         String path = this.getProductContentBasePath() + productContentName + this.product.getExtension();
         populatedProduct = this.content.getProduct(path);
         if (populatedProduct == null) {
            logger.warn("Could not find product content at path: {}", path);
            continue;
         }

         productVector.add(populatedProduct);
      }
      Product[] populatedProducts = productVector.toArray(new Product[productVector.size()]);

      // Sort and return
      Arrays.sort(populatedProducts);
      return populatedProducts;
   }

   private String[] getMasterProductList() {
      String[] productContentItemNames = this.content.getProductNames(this.getProductContentBasePath());
      return productContentItemNames;
   }

   /**
    * This is used for site navigation where, the product is pure content such as Business Insurance.
    */
   @Override
   public Product[] getAllProductsIncludingThoseNotInCatalogue() {

      // Obtain the list of product from the CMS
      String[] productContentItemNames = getMasterProductList();

      //logger.info("Current directory is: {}", System.getProperty("user.dir"));
      // Output when running maven clean install from project root:
      //    Current directory is: /Users/antony/projects/present

      // Build an array of populated products
      ArrayList<Product> productVector = new ArrayList<Product>();
      for (String productContentName : productContentItemNames) {

         // Populate the products with data from the CMS
         Product populatedProduct;
         String path = this.getProductContentBasePath() + productContentName + this.product.getExtension();
         populatedProduct = this.content.getProduct(path);
         productVector.add(populatedProduct);
      }
      Product[] populatedProducts = productVector.toArray(new Product[productVector.size()]);

      // Sort and return
      Arrays.sort(populatedProducts);
      return populatedProducts;
   }

   @Override
   public boolean hasProductType(final String productName) {
      // Check the product types for a matching product
      ProductType[] productTypes = this.catalogue.getProductTypes(ProductServiceImpl.REGION_NAME);
      for (ProductType productType : productTypes) {
         if (productName.equals(productType.getContentItemName())) {
            return true;
         }
         if (productName.equals(productType.getCatalogueName())) {
            return true;
         }
      }

      // Product not found
      return false;
   }

   @Override
   public ProductType getProductType(final String productName) {
      // Check the product types for a matching product
      ProductType[] productTypes = this.catalogue.getProductTypes(ProductServiceImpl.REGION_NAME);
      for (ProductType productType : productTypes) {
         if (productName.equals(productType.getContentItemName())) {
            return productType;
         }
         if (productName.equals(productType.getCatalogueName())) {
            return productType;
         }
      }

      // Product not found, cannot proceed
      throw new ContentException("Product content item name not found: [" + productName + "]", null);
   }

   @Override
   public AccountingPeriod[] getAccountingPeriods(final ProductType productType) {
      String catalogueName = productType.getCatalogueName();
      return getAccountingPeriodsForProductName(catalogueName);
   }

   public AccountingPeriod[] getAccountingPeriodsForProductName(final String catalogueName) {
      AccountingPeriod[] allAccountingPeriods;
      allAccountingPeriods = this.catalogue.getAvailableAccountingPeriods(catalogueName,
               ProductServiceImpl.REGION_NAME);

      // De-duplicate
      //uniqueAccountingPeriods = (AccountingPeriod[])(new LinkedHashSet<>(Arrays.asList(allAccountingPeriods)).toArray());
      LinkedHashSet<AccountingPeriod> uniqueAccountingPeriodsList = new LinkedHashSet<>(Arrays.asList(allAccountingPeriods));
      AccountingPeriod[] uniqueAccountingPeriods = new AccountingPeriod[uniqueAccountingPeriodsList.size()];
      int i=0;
      for(AccountingPeriod accountingPeriod : uniqueAccountingPeriodsList){
         uniqueAccountingPeriods[i] = accountingPeriod;
         i++;
      }
      Arrays.sort(uniqueAccountingPeriods);
      ArrayUtils.reverse(uniqueAccountingPeriods);
      return uniqueAccountingPeriods;
   }

   @Override
   public AccountingPeriod getAccountingPeriod(final ProductType productType, final String periodName) {
      AccountingPeriod[] accountingPeriods;
      accountingPeriods = this.catalogue.getAccountingPeriods(productType.getCatalogueName(),
               ProductServiceImpl.REGION_NAME);
      for (AccountingPeriod accountingPeriod : accountingPeriods) {
         if (accountingPeriod.getName().equals(periodName)) {
            return accountingPeriod;
         }
      }

      // Period not found, cannot proceed
      throw new ContentException("Accounting period name not found: [" + periodName + "] for product ["
               + productType.getContentItemName() + "]", null);
   }

   @Override
   public ProductComponent buildProductComponent(final Region region, final ProductType productType,
                                                 final AccountingPeriod accountingPeriod) {

      // Build the product component
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(region);
      productComponent.setProductType(productType);
      productComponent.setAccountingPeriod(accountingPeriod);

      return productComponent;
   }

   @Override
   public ProductComponent[][] getBundledProductComponents(final ProductComponent productComponent) {
      return this.catalogue.getBundledProductComponents(productComponent);
   }

   @Override
   public CommercialProduct getCommercialProduct(final Region region, final ProductComponent productComponent,
                                                 final String[] bundleNamePeriods) {

      ProductComponent[] bundledProductComponents;
      if (bundleNamePeriods != null) {
         bundledProductComponents = new ProductComponent[bundleNamePeriods.length];
         for (int i = 0; i < bundledProductComponents.length; i++) {
            String[] namePeriod = ProductServiceImpl.AMPERSAND_PATTERN.split(bundleNamePeriods[i]);
            String productContentItemName = namePeriod[0];
            String periodName = namePeriod[1];
            ProductType productType = this.getProductType(productContentItemName);
            AccountingPeriod accountingPeriod = this.getAccountingPeriod(productType, periodName);
            bundledProductComponents[i] = this.buildProductComponent(region, productType, accountingPeriod);
         }
      } else {
         bundledProductComponents = new ProductComponent[0];
      }

      return this.catalogue.getCommercialProduct(productComponent, bundledProductComponents);
   }

   @Override
   public CommercialProduct getCommercialProductByHash(final String hash) {
      return this.catalogue.getCommercialProductByHash(hash);
   }

   @Override
   public String getDownloadUrlForCommercialProduct(
         final String baseUrl,
         final CommercialProduct commercialProduct,
         final AvailableFormat availableFormat,
         final String extension) throws CatalogueServiceException {
      logger.debug("Generating download with stock URN [{}]", this.stockPath);
      String title = commercialProduct.calculateProductTitle(availableFormat);
      String url = this.catalogue.getDownloadUrlForCommercialProduct(baseUrl, title, extension);
      logger.info("Generated download URL [{}] for stock URN [{}]", url, this.stockPath);
      return url;
   }

   @Override
   public byte[] getBytesForUrn(final String assetUrn) throws CatalogueServiceException {
      logger.debug("Getting bytes for stock URN [{}]", assetUrn);
      byte[] bytes = this.catalogue.getBytesForUrn(assetUrn);
      logger.info("Read {} bytes for stock URN [{}]", bytes.length, assetUrn);
      return bytes;
   }

   @Override
   public CommercialProduct getCommercialProduct(final String regionName, final String productCatalogueName, final String accountingPeriodName) {
      CommercialProduct commercialProductToFind = this.catalogue.buildCommercialProduct(regionName, productCatalogueName, accountingPeriodName);
      String inventoryId = commercialProductToFind.getHash();
      return this.catalogue.getCommercialProductByHash(inventoryId);
   }

   @Override
   public Product getProduct(final String name) {
      String path = this.getProductContentBasePath() + name + this.product.getExtension();
      return this.content.getProduct(path);
   }

   @Override
   public Feature getFeature(final String name) {
      String path = this.getProductContentBasePath() + name + this.product.getExtension();
      return this.content.getFeature(path);
   }

   @Override
   public Feature[] getFeatures(final String[] featureNames) {
      Feature[] features = new Feature[featureNames.length];
      for (int i = 0; i < featureNames.length; i++) {
         String featureName = StringUtils.trim(featureNames[i]);
         if(StringUtils.isNoneBlank(featureName)) {
            String path = this.getProductContentBasePath() + featureName + this.feature.getExtension();
            features[i] = this.content.getFeature(path);
         }
      }
      Arrays.sort(features);
      return features;
   }

   @Override
   public Product[] getProducts(final String[] productNames) {
      Product[] products = new Product[productNames.length];
      for (int i = 0; i < productNames.length; i++) {
         String path = this.getProductContentBasePath() + productNames[i] + this.product.getExtension();
         products[i] = this.content.getProduct(path);
      }
      Arrays.sort(products);
      return products;
   }

   @Override
   public void setProductContentBasePath(final String productContentBasePath) {
      this.productContentBasePath = productContentBasePath;
   }

   @Override
   public String getProductContentBasePath() {
      return this.productContentBasePath;
   }

   @Override
   public String getCatalogueResource() {
      return this.catalogueResource;
   }

   @Override
   public void setCatalogueResource(final String catalogueResource) {
      this.catalogueResource = catalogueResource;
   }

   @Override
   public String getCatalogueBundlePriceResource() {
      return this.catalogueBundlePriceResource;
   }

   @Override
   public void setCatalogueBundlePriceResource(final String catalogueBundlePriceResource) {
      this.catalogueBundlePriceResource = catalogueBundlePriceResource;
   }

   @Override
   public String getCatalogueNamesResource() {
      return this.catalogueNamesResource;
   }

   @Override
   public void setCatalogueNamesResource(final String catalogueNamesResource) {
      this.catalogueNamesResource = catalogueNamesResource;
   }

   @Override
   public String getDescription(final CommercialProduct commercialProduct) {

      ProductType selectedProduct = commercialProduct.getProductComponent().getProductType();
      AccountingPeriod selectedPeriod = commercialProduct.getProductComponent().getAccountingPeriod();
      Set<ProductComponent> bundleList = commercialProduct.getBundledProductComponents();
      return selectedProduct.getCatalogueName() + " for Year to " + selectedPeriod.getName();
   }

	@Override
	public AvailableFormat[] getAvailableFormats(final CommercialProduct commercialProduct){
		return this.catalogue.getAvailableFormatsForCommercialProduct(commercialProduct);
	}

   public boolean isIngestCatalogue() {
      return ingestCatalogue;
   }

   public void setIngestCatalogue(final boolean ingestCatalogue) {
      this.ingestCatalogue = ingestCatalogue;
   }

   public ContentService getContent() {
      return content;
   }

   public void setContent(final ContentService content) {
      this.content = content;
   }

   public CatalogueService getCatalogue() {
      return catalogue;
   }

   public void setCatalogue(final CatalogueService catalogue) {
      this.catalogue = catalogue;
   }

   public FileLikePathService getFileLikePathService() {
      return fileLikePathService;
   }

   public void setFileLikePathService(final FileLikePathService fileLikePathService) {
      this.fileLikePathService = fileLikePathService;
   }

   public String getStockPath() {
      return stockPath;
   }

   public void setStockPath(final String stockPath) {
      this.stockPath = stockPath;
   }
}
