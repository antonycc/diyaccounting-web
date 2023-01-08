package uk.co.diyaccounting.web.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.web.content.Page;
import uk.co.diyaccounting.web.service.ContentServiceImpl;
import uk.co.diyaccounting.web.service.PageServiceImpl;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Tests for the page service mocking the content service where necessary
 * 
 * @author antony
 */
public class PageServiceTest {

   private static final String localPath = PageServiceImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";
   private FileLikePathService fileLikePathService = new FileLikePathService();

   @Test
   public void expectAListOfPages() throws ContentException {

      // Test parameters
      String pageContentBasePath = baseFileUrn + "test-content/";
      String prefix = pageContentBasePath;
      String path = "AboutPage";
      String[] paths = new String[1];
      paths[0] = path;
      String expectedTitle = "mock title";
      //ContentList contentList = new ContentList();
      //contentList.setContentList(paths);
      String[] contentList = {path};
      Page content = new Page();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getPageNames(pageContentBasePath)).andReturn(contentList).anyTimes();
      EasyMock.expect(contentService.getPage(prefix + path + ".md")).andReturn(content);
      EasyMock.replay(contentService);

      // Class under test
      PageServiceImpl classUnderTest = new PageServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setPageContentBasePath(pageContentBasePath);

      // Execute
      Page[] actualContent = classUnderTest.getAllPages();
      Assert.assertEquals(expectedTitle, actualContent[0].getTitle());
   }

   @Test
   public void expectHomePage() throws ContentException {

      // Test parameters
      String pageContentBasePath = baseFileUrn + "test-content/";
      String prefix = pageContentBasePath;
      String path = "HomePage";
      String expectedTitle = "mock title";
      Page content = new Page();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getPage(prefix + path + ".md")).andReturn(content).anyTimes();
      EasyMock.replay(contentService);

      // Class under test
      PageServiceImpl classUnderTest = new PageServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setPageContentBasePath(pageContentBasePath);

      // Execute
      Page actualContent = classUnderTest.getPage("HomePage");
      Assert.assertEquals(expectedTitle, actualContent.getTitle());
   }

   @Test
   public void expectAboutPage() throws ContentException {

      // Test parameters
      String pageContentBasePath = baseFileUrn + "test-content/";
      String prefix = pageContentBasePath;
      String path = "AboutPage";
      String expectedTitle = "mock title";
      Page content = new Page();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getPage(prefix + path + ".md")).andReturn(content);
      EasyMock.replay(contentService);

      // Class under test
      PageServiceImpl classUnderTest = new PageServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setPageContentBasePath(pageContentBasePath);

      // Execute
      Page actualContent = classUnderTest.getPage("AboutPage");
      Assert.assertEquals(expectedTitle, actualContent.getTitle());
   }

   @Test
   public void expectUrlNotToBeNull() throws ContentException {

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
}
