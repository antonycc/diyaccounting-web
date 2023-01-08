package uk.co.diyaccounting.web.integration;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import uk.co.diyaccounting.web.catalogue.*;
import uk.co.diyaccounting.web.service.CatalogueService;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.IOException;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest //(classes = TestSpringBootApplication.class)
@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(locations = { "classpath:/test-spring-context.xml" })
public class InMemoryIngestAndInterogationFromPublicInterface {

   private static final Logger logger = LoggerFactory.getLogger(InMemoryIngestAndInterogationFromPublicInterface.class);

   @Autowired
   @Qualifier("catalogue")
   private CatalogueService catalogue;

   @Autowired
   @Qualifier("fileLikePaths")
   private FileLikePathService file;

   private static final String localPath = InMemoryIngestAndInterogationFromPublicInterface.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static String catalogueResource = "urn:diyaccounting.co.uk:file://" + localPath + "catalogue/catalogue.txt";
   private static String catalogueNamesResource = "urn:diyaccounting.co.uk:file://" + localPath + "catalogue/catalogueNameContentItemName.properties";
   private static String catalogueBundlePriceResource = "urn:diyaccounting.co.uk:file://" + localPath + "catalogue/catalogueBundlePrices.properties";
   private static String stockLocation = "target/test-classes/test-stock-location/";
   private static String stockPath = "urn:diyaccounting.co.uk:file://" + stockLocation;

   @BeforeTestClass
   public void ingestCatalogue() {
      this.catalogue.configureCatalogue(this.catalogueBundlePriceResource, this.catalogueNamesResource, this.catalogueResource, this.stockPath);
      this.catalogue.populateCatalogue(this.file);
   }

   /**
    * Do nothing in the test but ingest the catalogue before and clear it after
    */
   @Test
   public void testNOP() {
      logger.info("****************");
      logger.info("** EMPTY TEST **");
      logger.info("****************");
   }

   @Test
   public void expectToFindCommercialProductFromInventoryId()
         throws IOException{

      // Test parameters
      String regionName = "GB";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriodName = "2018-04-05 (Apr18)";

      // Expected Results

      // Mocks

      // Class under test
      CatalogueService classUnderTest = this.catalogue;
      Assert.assertNotNull(classUnderTest);

      // Execution
      CommercialProduct commercialProductToFind = classUnderTest.buildCommercialProduct(regionName, productCatalogueName, accountingPeriodName);
      String inventoryNumber = commercialProductToFind.getHash();
      CommercialProduct foundCommercialProduct = classUnderTest.getCommercialProductByHash(inventoryNumber);

      // Checks
      Assert.assertNotNull(foundCommercialProduct);
      Assert.assertEquals(regionName, foundCommercialProduct.getProductComponent().getRegion().getName());
      Assert.assertEquals(productCatalogueName, foundCommercialProduct.getProductComponent().getProductType().getCatalogueName());
      Assert.assertEquals(accountingPeriodName, foundCommercialProduct.getProductComponent().getAccountingPeriod().getName());
   }

   /**
    * Get all the product type names Get all the periods for a named product Get the product for a given type and period
    */
   @Test
   public void testCatalogueServiceAccess() throws Exception {
      logger.debug("Get all the product type names");

      // Test parameters
      String regionName = "GB";
      String productTypeCatalogueItemName = "Basic Sole Trader";
      String productType = "Basic Sole Trader";
      String accountingPeriodName = "2018-04-05 (Apr18)";

      // Expected results
      int expectedProductTypes = 6;
      String expectedFirstProductTypeCatalogueName = "Basic Sole Trader";
      String expectedFirstProductTypeContentItemName = "BasicSoleTraderProduct";
      int expectedPeriods = 1;
      String expectedFirstCataloguePeriodName = "2018-04-05 (Apr18)";
      String expectedCataloguePeriodName = accountingPeriodName;
      String expectedProductTypeCatalogueItemName = productType;
      // String expectedDownloadUrlFilename = "1112basica1as.htm";
      Region region = new Region();
      region.setName("GB");
      AccountingPeriod expectedAccountingPeriod = new AccountingPeriod();
      expectedAccountingPeriod.setName(accountingPeriodName);
      ProductType expectedProductType = new ProductType();
      expectedProductType.setCatalogueName(expectedProductTypeCatalogueItemName);
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(region);
      productComponent.setProductType(expectedProductType);
      productComponent.setAccountingPeriod(expectedAccountingPeriod);
      ProductComponent[] bundledProductComponents = new ProductComponent[0];

      // Expect a specific number of product types
      ProductType[] productTypes = this.catalogue.getProductTypes(regionName);
      Assert.assertNotNull(productTypes);
      Assert.assertTrue("There should be the expected number of product types", productTypes.length >= expectedProductTypes);
      ProductType foundProductType = null;
      for(ProductType candidateProductType : productTypes){
         Assert.assertNotNull(candidateProductType);
         if(expectedFirstProductTypeCatalogueName.equals(candidateProductType.getCatalogueName())){
            foundProductType = candidateProductType;
            break;
         }
      }
      Assert.assertNotNull(foundProductType);
      Assert.assertEquals(expectedFirstProductTypeContentItemName,
               foundProductType.getContentItemName());

      // Expect a specific period
      AccountingPeriod[] accountingPeriods = this.catalogue.getAvailableAccountingPeriods(productTypeCatalogueItemName,
               regionName);
      Assert.assertNotNull(accountingPeriods);
      Assert.assertTrue("There should be the expected number of periods", accountingPeriods.length >= expectedPeriods);
      //accountingPeriods[0].setHashParameters(null);
      AccountingPeriod firstAccountingPeriod = accountingPeriods[0];
      Assert.assertNotNull("The first period should not be null", firstAccountingPeriod);

      // Get the commercial product for a specific component and hash
      CommercialProduct commercialProduct = this.catalogue.getCommercialProduct(productComponent,
               bundledProductComponents);
      Assert.assertNotNull(commercialProduct.getProductComponent());
      Assert.assertNotNull(commercialProduct.getHash());
      CommercialProduct commercialProduct2 = this.catalogue.getCommercialProductByHash(commercialProduct.getHash());

      // Check
      Assert.assertNotNull(commercialProduct);
      Assert.assertEquals("The period should be named as expected", expectedCataloguePeriodName,
               commercialProduct.getProductComponent().getAccountingPeriod().getName());
      Assert.assertEquals("The product should be named as expected", expectedProductType.getCatalogueName(),
               commercialProduct.getProductComponent().getProductType().getCatalogueName());
      Assert.assertEquals("The id should be the same in each", commercialProduct.getId(), commercialProduct2.getId());
      Assert.assertNotNull("The headline in product 2 should not be null",
               commercialProduct2.getProductComponent());
      Assert.assertEquals("The headline should be the same in each",
               commercialProduct.getProductComponent().getProductType().getCatalogueName(),
               commercialProduct2.getProductComponent().getProductType().getCatalogueName());

   }
}