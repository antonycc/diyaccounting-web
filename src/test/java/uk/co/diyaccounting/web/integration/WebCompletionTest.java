package uk.co.diyaccounting.web.integration;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import uk.co.diyaccounting.web.controller.ContentApi;
import uk.co.diyaccounting.web.service.CatalogueService;
import uk.co.diyaccounting.web.service.CatalogueServiceMemImpl;
import uk.co.diyaccounting.web.service.PageService;
import uk.co.diyaccounting.web.service.ProductService;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.time.TimeService;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

/**
 * Complete sales and expect to find a record stored on completion
 * 
 * @author Antony
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:/spring-context.xml" })
@SpringBootTest
public class WebCompletionTest {

   private static final Logger logger = LoggerFactory.getLogger(WebCompletionTest.class);

   @Autowired
   @Qualifier("contentApi")
   private ContentApi contentApi;

   @Autowired
   @Qualifier("fileLikePaths")
   private FileLikePathService fileLikePathService;

   @Autowired
   @Qualifier("catalogue")
   private CatalogueService catalogue;

   private TimeService time = new TimeService();

   private final String localPath = CatalogueServiceMemImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String basePath = "file://" + this.localPath + "../../src/main/resources/";
   private final String baseCataloguePath = "file://" + this.localPath + "../../src/main/resources/";
   private final String catalogueResource = "urn:diyaccounting.co.uk:" + this.baseCataloguePath + "test-catalogue/catalogue.txt";
   private final String catalogueNamesResource = "urn:diyaccounting.co.uk:" + this.baseCataloguePath + "test-catalogue/catalogueNameContentItemName.properties";
   private final String catalogueBundlePriceResource = "urn:diyaccounting.co.uk:" + this.baseCataloguePath + "test-catalogue/catalogueBundlePrices.properties";
   private final String productContentBasePath = "urn:diyaccounting.co.uk:" + this.basePath + "test-content/";
   private final String pageContentBasePath = "urn:diyaccounting.co.uk:" + this.basePath + "test-content/";
   private final String stockLocation = "/stock/";
   private final String stockPath = "urn:diyaccounting.co.uk:classpath:" + this.stockLocation;

   @BeforeTestClass
   public void ingestCatalogue() throws IOException, ParseException {
      this.catalogue.configureCatalogue(this.catalogueBundlePriceResource,
            this.catalogueNamesResource,
            this.catalogueResource,
            this.stockPath);
      this.catalogue.populateCatalogue(this.fileLikePathService);
   }

   @Test
   public void dummyTest() {
      // NOP
   }

   //@Test
   // TODO" Fix test for expecting url not to be null
   public void expectUrlNotToBeNull() throws ContentException {

      String localPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      //String basePath = localPath + "../../../gb-web/src/test/resources/";
      String productContentBasePath = "urn:diyaccounting.co.uk:file://" + localPath + "test-content/";
      String pageContentBasePath = "urn:diyaccounting.co.uk:file://" + localPath + "test-content/";

      // Expected values

      // Create Object to test
      ProductService productService = this.contentApi.getProductContent();
      String savedProductContentBasePath = productService.getProductContentBasePath();
      PageService pageService = this.contentApi.getPageContent();
      String savedPageContentBasePath = pageService.getPageContentBasePath();
      HashMap<String, Object> model;
      try {
         productService.setProductContentBasePath(productContentBasePath);
         pageService.setPageContentBasePath(pageContentBasePath);
         model = this.contentApi.page(null);
      }finally {
         productService.setProductContentBasePath(savedProductContentBasePath);
         pageService.setPageContentBasePath(savedPageContentBasePath);
      }

      // Check
      Assert.assertNotNull("Expected Api controller to be wired", this.contentApi);
      Assert.assertNotNull("Expected model not to be Null", model);
      @SuppressWarnings("unchecked")
      HashMap<String, Object> pageMap = (HashMap<String, Object>) model.get("pages");
      Assert.assertNotNull("Expected pages not to be Null", pageMap);
      Assert.assertNotNull("Expected pages not to be Null", pageMap.get("home"));
   }

}
