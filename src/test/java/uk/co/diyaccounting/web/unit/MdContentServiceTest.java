package uk.co.diyaccounting.web.unit;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.diyaccounting.web.content.Article;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Page;
import uk.co.diyaccounting.web.content.Product;
import uk.co.diyaccounting.web.service.ContentServiceImpl;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Tests for the content service
 *
 * @author Antony
 */
public class MdContentServiceTest {

   private static final Logger logger = LoggerFactory.getLogger(MdContentServiceTest.class);

   private static final String localPath = ContentServiceImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";
   private FileLikePathService fileLikePathService = new FileLikePathService();

   @Test
   public void expectToGetAPageContentItem() throws ContentException {

      // Test parameters
      String path = baseFileUrn + "test-content/AboutPage.md";
      String expectedShortTitle = "About DIY Accounting";

      // Mocks

      // Class under test
      ContentServiceImpl classUnderTest = new ContentServiceImpl();
      classUnderTest.setFileHelper(this.fileLikePathService);

      // Execute
      Page actualPage = classUnderTest.getPage(path);
      Assert.assertEquals(expectedShortTitle, actualPage.getShortTitle());
   }

   @Test
   public void expectToGetAProductContentItem() throws ContentException {

      // Test parameters
      String path = baseFileUrn + "test-content/BasicSoleTraderProduct.md";
      String expectedShortTitle = "Basic Sole Trader";

      // Mocks

      // Class under test
      ContentServiceImpl classUnderTest = new ContentServiceImpl();
      classUnderTest.setFileHelper(this.fileLikePathService);

      // Execute
      Product actualProduct = classUnderTest.getProduct(path);
      Assert.assertEquals(expectedShortTitle, actualProduct.getShortTitle());
   }

   @Test
   public void expectToGetAPartnerProductContentItem() throws ContentException {

      // Test parameters
      String path = baseFileUrn + "test-content/BusinessInsuranceProduct.md";
      String expectedShortTitle = "Business Insurance";
      String expectedFulfillmentUrl = "http://www.policybee.co.uk/?partner=35";

      // Mocks

      // Class under test
      ContentServiceImpl classUnderTest = new ContentServiceImpl();
      classUnderTest.setFileHelper(this.fileLikePathService);

      // Execute
      Product actualProduct = classUnderTest.getProduct(path);
      Assert.assertEquals(expectedShortTitle, actualProduct.getShortTitle());
      Assert.assertEquals(expectedFulfillmentUrl, actualProduct.getFulfillmentUrl());
   }

   @Test
   public void expectToGetAFeatureContentItem() throws ContentException {

      // Test parameters
      String path = baseFileUrn + "test-content/TaxiVATFeature.md";
      String expectedShortTitle = "VAT";

      // Mocks

      // Class under test
      ContentServiceImpl classUnderTest = new ContentServiceImpl();
      classUnderTest.setFileHelper(this.fileLikePathService);

      // Execute
      Feature actualFeature = classUnderTest.getFeature(path);
      Assert.assertEquals(expectedShortTitle, actualFeature.getShortTitle());
   }

   @Test
   public void expectToGetAContentListContentItem() throws ContentException {

      // Test parameters
      String path = baseFileUrn + "test-content/";
      String filter = "Product.md";
      int expectedLength = 6;

      // Mocks

      // Class under test
      ContentServiceImpl classUnderTest = new ContentServiceImpl();
      classUnderTest.setFileHelper(this.fileLikePathService);

      // Execute
      String[] actualContentList = classUnderTest.getProductNames(path);
      Assert.assertEquals(expectedLength, actualContentList.length);
   }

   @Test
   public void expectToGetAnArticleContentItem() throws ContentException {

      // Test parameters
      String path = baseFileUrn + "test-content/DoubleEntryBookkeepingArticle.md";
      String expectedTitle = "Double Entry Bookkeeping May Be The Only Option";

      // Mocks

      // Class under test
      ContentServiceImpl classUnderTest = new ContentServiceImpl();
      classUnderTest.setFileHelper(this.fileLikePathService);

      // Execute
      Article actualArticle = classUnderTest.getArticle(path);
      Assert.assertEquals(expectedTitle, actualArticle.getTitle());
   }

}
