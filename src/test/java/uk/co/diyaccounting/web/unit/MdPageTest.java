package uk.co.diyaccounting.web.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.web.content.Page;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Reads a content item from a Markdown classpath resource
 * @author Antony
 */
public class MdPageTest {

   private static final String localPath = Page.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(PageTest.class);

   private FileLikePathService fileLikePathService = new FileLikePathService();

   @Test
   public void expectAttributesToPreseveSetValues() throws ContentException {

      String testPath = baseFileUrn + "src/test/path/TestPage.md";

      // Expected values
      String expectedTitle = "Test Title";
      String expectedDescription = "Test Description";
      String expectedMetaDescription = "Description for search results";
      String expectedKeywords = "Test Keywords";

      // Create Object to test
      Page page = new Page();
      page.setPath(testPath);
      page.populateContent(this.fileLikePathService);
      page.setImage(null);
      page.populateContent(this.fileLikePathService);

      // Check
      Assert.assertEquals(expectedTitle, page.getTitle());
      Assert.assertEquals(expectedDescription, page.getDescription());
      Assert.assertEquals(expectedMetaDescription, page.getMetaDescription());
      Assert.assertEquals(expectedKeywords, page.getKeywords());

      // Trivial checks to exercise the code
      Assert.assertFalse(page.getFeatured());
      Assert.assertFalse(page.getFeaturedPartner());
      Assert.assertNotNull(page.getTrailingBody());
      //Assert.assertEquals("Page", page.getType());
   }

   @Test
   public void expectSpecificContent() throws ContentException {

      String testPath = baseFileUrn + "src/test/path/TestPage.md";

      // Expected values
      String expectedTitle = "X";
      String expectedDescription = "Y";
      String expectedKeywords = "Z";

      // Create Object to test
      Page page = new Page();
      page.setPath(testPath);
      page.populateContent(this.fileLikePathService);

      // Check
      Assert.assertFalse(expectedTitle.equals(page.getTitle()));
      Assert.assertFalse(expectedDescription.equals(page.getDescription()));
      Assert.assertFalse(expectedKeywords.equals(page.getKeywords()));
   }

   @Test
   public void expectUrlNotToBeNullAbout() throws ContentException {

      String aboutPath = baseFileUrn + "test-content/AboutPage.md";

      // Expected values
      // String path = "path";

      // Create Object to test
      Page page = new Page();
      page.setPath(aboutPath);
      page.populateContent(this.fileLikePathService);
      page.postPopulationConfig();

      // Check
      Assert.assertNotNull("Expected Url not to be Null", page.getImage().getSrcPath());
   }

   @Test
   public void expectUrlNotToBeNullProducts() throws ContentException {

      String aboutPath = baseFileUrn + "test-content/ProductsPage.md";

      // Expected values
      // String path = "path";

      // Create Object to test
      Page page = new Page();
      page.setPath(aboutPath);
      page.populateContent(this.fileLikePathService);
      page.postPopulationConfig();

      // Check
      Assert.assertNotNull("Expected Url not to be Null", page.getImage().getSrcPath());
   }

}
