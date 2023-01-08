package uk.co.diyaccounting.web.integration;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import uk.co.diyaccounting.web.catalogue.CommercialProduct;
import uk.co.diyaccounting.web.content.Product;
import uk.co.diyaccounting.web.service.ProductService;
import uk.co.diyaccounting.web.unit.MdPageTest;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.IOException;

/**
 * Complete sales and expect to find a record stored on completion
 * 
 * @author Antony
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:/content-spring-context.xml" })
@SpringBootTest
public class ProductCatalogueTnotest {

   private static final Logger logger = LoggerFactory.getLogger(ProductCatalogueTnotest.class);

   @Autowired
   @Qualifier("productContent")
   private ProductService productContent;

   @Autowired
   @Qualifier("fileLikePaths")
   private FileLikePathService file;

   private static final String localPath = MdPageTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";
   //private static String localCataloguePath = ProductCatalogueTnotest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   //private static String baseCataloguePath = "urn:diyaccounting.co.uk:" + "file://" + localCataloguePath + "../../../gb-web-local/src/main/resources/";
   //private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:url:http://localhost:8090/";
   private static String baseCataloguePath = baseFileUrn;
   private static String catalogueResource = baseCataloguePath + "catalogue/catalogue.txt";
   private static String catalogueNamesResource = baseCataloguePath + "catalogue/catalogueNameContentItemName.properties";
   private static String catalogueBundlePriceResource = baseCataloguePath + "catalogue/catalogueBundlePrices.properties";
   //private static String localPath = ProductCatalogueTnotest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   //private static String baseFileUrn = "urn:diyaccounting.co.uk:" + localPath + "../../../gb-web-local/src/main/resources/";
   private static String productContentBasePath = baseFileUrn + "test-content/";

   @BeforeTestClass
   public void ingestCatalogue() {
      this.productContent.populateCatalogue();
   }

   // TODO: Fix this test
   //@Test
   public void expectToFindProductContent()
         throws IOException{

      // Test parameters
      String productCatalogueName = "BasicSoleTraderProduct";

      // Expected Results

      // Mocks

      // Class under test
      ProductService classUnderTest = this.productContent;
      Assert.assertNotNull(classUnderTest);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execution
      Product product = classUnderTest.getProduct(productCatalogueName);

      // Checks
      Assert.assertNotNull(product);
      Assert.assertEquals(productCatalogueName, product.getName());
   }

   // TODO: Fix this test
   //@Test
   public void expectToFindCommercialProductFromInventoryId()
         throws IOException{

      // Test parameters
      String regionName = "GB";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriodName = "2021-04-05 (Apr21)";

      // Expected Results

      // Mocks

      // Class under test
      ProductService classUnderTest = this.productContent;
      Assert.assertNotNull(classUnderTest);

      // Execution
      CommercialProduct foundCommercialProduct = classUnderTest.getCommercialProduct(regionName, productCatalogueName, accountingPeriodName);

      // Checks
      Assert.assertNotNull(foundCommercialProduct);
      Assert.assertEquals(regionName, foundCommercialProduct.getProductComponent().getRegion().getName());
      Assert.assertEquals(productCatalogueName, foundCommercialProduct.getProductComponent().getProductType().getCatalogueName());
      Assert.assertEquals(accountingPeriodName, foundCommercialProduct.getProductComponent().getAccountingPeriod().getName());
   }

   //@Test
   public void expectToFindProducts()
           throws IOException{

      // TODO: Also find pages

      // Test parameters
      //String productCatalogueName = "BasicSoleTraderProduct";

      // Expected Results

      // Mocks

      // Class under test
      ProductService classUnderTest = this.productContent;
      Assert.assertNotNull(classUnderTest);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execution
      Product[] products = classUnderTest.getAllProducts();

      // Checks
      Assert.assertNotNull(products);
      Assert.assertTrue(products.length > 0);
   }
}
