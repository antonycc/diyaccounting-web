package uk.co.diyaccounting.web.unit;

import jakarta.servlet.http.HttpServletResponse;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import uk.co.diyaccounting.web.DiyAccountingSpringBootConfiguration;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Product;
import uk.co.diyaccounting.web.controller.ControllerUtils;
import uk.co.diyaccounting.web.controller.SiteMapController;
import uk.co.diyaccounting.web.service.ArticleService;
import uk.co.diyaccounting.web.service.PageService;
import uk.co.diyaccounting.web.service.ProductService;

/**
 * Tests for the image controller
 * 
 * @author antony
 */
public class SiteMapControllerTest {

   @Test
   public void expectASiteMapWithNoProducts() throws Exception {

      // Test parameters
      String expectedViewName = "sitemap";
      String expectedContentType = MediaType.TEXT_XML_VALUE;

      // Expected data
      Product[] expectedProducts = new Product[0];
      Feature[] expectedFeatures = new Feature[0];
      ControllerUtils controllerUtils = new ControllerUtils();
      HttpServletResponse response = new MockHttpServletResponse();
      DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(diyAccountingSpringBootConfiguration);

      // Mocks
      PageService pageService = EasyMock.createNiceMock(PageService.class);
      ProductService product = EasyMock.createNiceMock(ProductService.class);
      EasyMock.expect(product.getAllProducts()).andReturn(expectedProducts);
      ArticleService article = EasyMock.createNiceMock(ArticleService.class);
      EasyMock.replay(pageService, product, article);

      // Class under test
      SiteMapController classUnderTest = new SiteMapController();
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(product);
      classUnderTest.setArticleContent(article);
      classUnderTest.setControllerUtils(controllerUtils);

      // Execute
      ModelAndView actualSitemap = classUnderTest.sitemap(response);
      Assert.assertEquals(expectedViewName, actualSitemap.getViewName());
      Assert.assertNotNull(actualSitemap.getModelMap().get("products"));
      Assert.assertEquals(expectedFeatures.length, ((Product[]) (actualSitemap.getModelMap().get("products"))).length);
      Assert.assertNotNull(actualSitemap.getModelMap().get("features"));
      Assert.assertEquals(expectedFeatures.length, ((Feature[]) (actualSitemap.getModelMap().get("features"))).length);
      Assert.assertEquals(expectedContentType, response.getContentType());
   }

   @Test
   public void expectASiteMapWithOneProductButNoFeatures() throws Exception {

      // Test parameters
      String expectedViewName = "sitemap";
      Product product1 = new Product();
      String expectedContentType = MediaType.TEXT_XML_VALUE;

      // Expected data
      Product[] expectedProducts = { product1 };
      Feature[] expectedFeatures = new Feature[0];
      ControllerUtils controllerUtils = new ControllerUtils();
      HttpServletResponse response = new MockHttpServletResponse();
      DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(diyAccountingSpringBootConfiguration);

      // Mocks
      PageService pageService = EasyMock.createNiceMock(PageService.class);
      ProductService product = EasyMock.createNiceMock(ProductService.class);
      EasyMock.expect(product.getAllProducts()).andReturn(expectedProducts);
      ArticleService article = EasyMock.createNiceMock(ArticleService.class);
      EasyMock.replay(pageService, product, article);

      // Class under test
      SiteMapController classUnderTest = new SiteMapController();
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(product);
      classUnderTest.setArticleContent(article);
      classUnderTest.setControllerUtils(controllerUtils);

      // Execute
      ModelAndView actualSitemap = classUnderTest.sitemap(response);
      Assert.assertEquals(expectedViewName, actualSitemap.getViewName());
      Assert.assertNotNull(actualSitemap.getModelMap().get("products"));
      Assert.assertEquals(expectedProducts, actualSitemap.getModelMap().get("products"));
      Assert.assertNotNull(actualSitemap.getModelMap().get("features"));
      Assert.assertEquals(expectedFeatures.length, ((Feature[]) (actualSitemap.getModelMap().get("features"))).length);
      Assert.assertEquals(expectedContentType, response.getContentType());
   }

   @Test
   public void expectASiteMapWithOneProductAndOneFeature() throws Exception {

      // Test parameters
      String expectedViewName = "sitemap";
      Product product1 = new Product();
      Feature feature1 = new Feature();
      String feature1Name = "ProfitAndLossFeature";
      feature1.setName(feature1Name);
      String expectedContentType = MediaType.TEXT_XML_VALUE;

      // Expected data
      Product[] expectedProducts = { product1 };
      Feature[] expectedFeatures = { feature1 };
      String[] featureNames = { feature1.getName() };
      product1.setFeatureNames(featureNames);
      ControllerUtils controllerUtils = new ControllerUtils();
      HttpServletResponse response = new MockHttpServletResponse();
      DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(diyAccountingSpringBootConfiguration);

      // Mocks
      PageService pageService = EasyMock.createNiceMock(PageService.class);
      ProductService product = EasyMock.createNiceMock(ProductService.class);
      EasyMock.expect(product.getAllProducts()).andReturn(expectedProducts);
      EasyMock.expect(product.getFeatures((String[]) EasyMock.anyObject())).andReturn(expectedFeatures);
      ArticleService article = EasyMock.createNiceMock(ArticleService.class);
      EasyMock.replay(pageService, product, article);

      // Class under test
      SiteMapController classUnderTest = new SiteMapController();
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(product);
      classUnderTest.setArticleContent(article);
      classUnderTest.setControllerUtils(controllerUtils);

      // Execute
      ModelAndView actualSitemap = classUnderTest.sitemap(response);
      Assert.assertEquals(expectedViewName, actualSitemap.getViewName());
      Assert.assertNotNull(actualSitemap.getModelMap().get("products"));
      Assert.assertEquals(expectedProducts, actualSitemap.getModelMap().get("products"));
      Assert.assertNotNull(actualSitemap.getModelMap().get("features"));
      Assert.assertEquals(expectedFeatures.length, ((Feature[]) (actualSitemap.getModelMap().get("features"))).length);
      Assert.assertEquals(feature1, ((Feature[]) (actualSitemap.getModelMap().get("features")))[0]);
      Assert.assertEquals(expectedContentType, response.getContentType());
   }
}
