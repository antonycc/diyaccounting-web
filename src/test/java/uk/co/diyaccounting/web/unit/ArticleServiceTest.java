package uk.co.diyaccounting.web.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.web.content.Article;
import uk.co.diyaccounting.web.service.ArticleServiceImpl;
import uk.co.diyaccounting.web.service.ContentServiceImpl;
import uk.co.polycode.mdcms.cms.service.ContentException;

/**
 * Tests for the article service mocking the content service where necessary
 * 
 * @author antony
 */
public class ArticleServiceTest {

   private static final String localPath = ArticleServiceTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectAListOfArticles() throws ContentException {

      // Test parameters
      String articleContentBasePath = baseFileUrn + "test-content/";
      String prefix = articleContentBasePath;
      String articleFilter = "Article.md";
      String path = "DoubleEntryBookkeepingArticle";
      String[] paths = new String[1];
      paths[0] = path;
      String expectedTitle = "mock title";
      String[] contentList = {path};
      Article content = new Article();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getArticleNames(articleContentBasePath)).andReturn(contentList).anyTimes();
      EasyMock.expect(contentService.getArticle(prefix + path + ".md")).andReturn(content);
      EasyMock.replay(contentService);

      // Class under test
      ArticleServiceImpl classUnderTest = new ArticleServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setArticleContentBasePath(articleContentBasePath);

      // Execute
      Article[] actualArticles = classUnderTest.getAllArticles();
      Assert.assertEquals(expectedTitle, actualArticles[0].getTitle());
   }

   @Test
   public void expectAnArticle() throws ContentException {

      // Test parameters
      String articleContentBasePath = baseFileUrn + "test-content/";
      String prefix = articleContentBasePath;
      String path = "DoubleEntryBookkeepingArticle";
      String expectedTitle = "mock title";
      Article content = new Article();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getArticle(prefix + path + ".md")).andReturn(content);
      EasyMock.replay(contentService);

      // Class under test
      ArticleServiceImpl classUnderTest = new ArticleServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setArticleContentBasePath(articleContentBasePath);

      // Execute
      Article actualArticle = classUnderTest.getArticle(path);
      Assert.assertEquals(expectedTitle, actualArticle.getTitle());
   }

}
