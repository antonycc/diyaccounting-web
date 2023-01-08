package uk.co.diyaccounting.web.unit;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.text.WordUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import uk.co.diyaccounting.web.DiyAccountingSpringBootConfiguration;
import uk.co.diyaccounting.web.catalogue.*;
import uk.co.diyaccounting.web.content.Article;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Page;
import uk.co.diyaccounting.web.content.Product;
import uk.co.diyaccounting.web.controller.ContentApi;
import uk.co.diyaccounting.web.controller.ControllerUtils;
import uk.co.diyaccounting.web.model.Site;
import uk.co.diyaccounting.web.ops.ProductTestUtils;
import uk.co.diyaccounting.web.service.ArticleService;
import uk.co.diyaccounting.web.service.PageService;
import uk.co.diyaccounting.web.service.ProductService;
import uk.co.diyaccounting.web.service.ProductServiceImpl;
import uk.co.polycode.mdcms.cms.dto.Image;
import uk.co.polycode.mdcms.cms.dto.NameBody;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

public class ContentApiTest {

   @Test
   public void expectPageResources() {

      // Test parameters
      String pageId = "about";
      String type = "page";
      String pageTitle = "mock title";
      int expectedHttpStatusCode = HttpStatus.OK.value();
      String imageSrc = UUID.randomUUID().toString();

      // Expected data
      String pageName = WordUtils.capitalize(pageId) + WordUtils.capitalize(type);
      Page content = new Page();
      content.setTitle(pageTitle);
      content.setName(pageName);
      NameBody info = new Page();
      info.setName(content.getName());
      info.setBody(content.getBody());
      NameBody contact = new Page();
      contact.setName(content.getName());
      contact.setBody(content.getBody());
      Image image = new Image("<img src=\"" + imageSrc + "\" />");
      content.setImage(image);
      HttpServletRequest request = new MockHttpServletRequest();
      ControllerUtils controllerUtils = new ControllerUtils();
      DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(diyAccountingSpringBootConfiguration);
      Vector<Field> fields = new Vector<>();
      NameBody[] testimonials = new NameBody[0];

      // Mocks
      PageService pageService = EasyMock.createNiceMock(PageService.class);
      Page[] pages = {content};
      EasyMock.expect(pageService.getAllPages()).andReturn(pages).anyTimes();
      EasyMock.expect(pageService.getPage(EasyMock.anyString())).andReturn(content).anyTimes();

      ArticleService articleService = EasyMock.createNiceMock(ArticleService.class);

      ProductService productService = EasyMock.createNiceMock(ProductService.class);

      //TranslationService translationService = EasyMock.createNiceMock(TranslationService.class);
      //EasyMock.expect(translationService.getTranslateableFields(EasyMock.anyObject())).andReturn(fields)
      //         .anyTimes();

      EasyMock.replay(articleService, pageService, productService);
      //controllerUtils.setTranslation(translationService);

      // Class under test
      ContentApi classUnderTest = new ContentApi();
      classUnderTest.setControllerUtils(controllerUtils);
      classUnderTest.setArticleContent(articleService);
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(productService);

      // Execute
      HashMap<String, Object> modelMap = classUnderTest.pageWithArticles(request);

      // Checks
      Assert.assertNotNull(classUnderTest.getPageContent());
      Assert.assertNotNull(modelMap);
      Assert.assertTrue(modelMap.containsKey("pages"));
      HashMap<String, Object> pagesMap = (HashMap<String, Object>)modelMap.get("pages");
      Assert.assertTrue(pagesMap.containsKey(pageId));
      Assert.assertNotNull(pagesMap.get(pageId));
      Assert.assertEquals(pageTitle, ((Page) pagesMap.get(pageId)).getTitle());
      Assert.assertNotNull(((Page) pagesMap.get(pageId)).getImage());
      //Assert.assertTrue(((Page) pagesMap.get(pageId)).getImage().getSrc().contains(imageSrc));
   }

   @Test
   public void expectArticle() {

      // Test parameters
      String pageId = "article";
      String type = "article";
      String pageTitle = "mock title";
      int expectedHttpStatusCode = HttpStatus.OK.value();
      String articleName = "TestArticle";
      boolean hasProductType = true;

      // Expected data
      String pageName = WordUtils.capitalize(pageId) + WordUtils.capitalize(type);
      Article article = new Article();
      article.setTitle(pageTitle);
      article.setName(articleName);
      HttpServletRequest request = new MockHttpServletRequest();
      ControllerUtils controllerUtils = new ControllerUtils();
      DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(diyAccountingSpringBootConfiguration);
      Vector<Field> fields = new Vector<>();

      PageService pageService = EasyMock.createNiceMock(PageService.class);

      ArticleService articleService = EasyMock.createNiceMock(ArticleService.class);
      EasyMock.expect(articleService.getArticle(articleName)).andReturn(article).anyTimes();

      ProductService productService = EasyMock.createNiceMock(ProductService.class);

      //TranslationService translationService = EasyMock.createNiceMock(TranslationService.class);
      //EasyMock.expect(translationService.getTranslateableFields(EasyMock.anyObject())).andReturn(fields)
      //     .anyTimes();

      EasyMock.replay(articleService, pageService, productService);
      //controllerUtils.setTranslation(translationService);

      // Class under test
      ContentApi classUnderTest = new ContentApi();
      classUnderTest.setControllerUtils(controllerUtils);
      classUnderTest.setArticleContent(articleService);
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(productService);

      // Execute
      HashMap<String, Object> modelMap = classUnderTest.article(request, articleName);
      Assert.assertTrue(modelMap.containsKey("pages"));
      HashMap<String, Object> pagesMap = (HashMap<String, Object> )modelMap.get("pages");
      Assert.assertTrue(pagesMap.containsKey(pageId));
      Assert.assertNotNull(pagesMap.get(pageId));
      String actualPageTitle = ((Article) pagesMap.get(pageId)).getTitle();
      Assert.assertEquals(pageTitle, actualPageTitle);
   }

   @Test
   public void expectFeaturePageForNullProduct() {

      // Test parameters
      String pageId = "feature";
      String type = "feature";
      String pageTitle = "mock title";
      int expectedHttpStatusCode = HttpStatus.OK.value();
      String featureName = "ProfitAndLossFeature";

      // Expected data
      String pageName = WordUtils.capitalize(pageId) + WordUtils.capitalize(type);
      Feature content = new Feature();
      content.setTitle(pageTitle);
      content.setName(pageName);
      HttpServletRequest request = new MockHttpServletRequest();
      ControllerUtils controllerUtils = new ControllerUtils();
      DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(diyAccountingSpringBootConfiguration);
      Vector<Field> fields = new Vector<>();

      // Mocks
      PageService pageService = EasyMock.createNiceMock(PageService.class);

      ProductService productService = EasyMock.createNiceMock(ProductService.class);
      EasyMock.expect(productService.getFeature(featureName)).andReturn(content);

      //TranslationService translationService = EasyMock.createNiceMock(TranslationService.class);
      //EasyMock.expect(translationService.getTranslateableFields(EasyMock.anyObject())).andReturn(fields)
      //      .anyTimes();

      EasyMock.replay(pageService, productService);
      //controllerUtils.setTranslation(translationService);

      // Class under test
      ContentApi classUnderTest = new ContentApi();
      classUnderTest.setControllerUtils(controllerUtils);
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(productService);

      // Execute
      HashMap<String, Object> modelMap = classUnderTest.feature(request, featureName);
      Assert.assertTrue(modelMap.containsKey("pages"));
      HashMap<String, Object> pagesMap = (HashMap<String, Object> )modelMap.get("pages");
      Assert.assertTrue(pagesMap.containsKey(pageId));
      Assert.assertNotNull(pagesMap.get(pageId));
      String actualPageTitle = ((Feature) pagesMap.get(pageId)).getTitle();
      Assert.assertEquals(pageTitle, actualPageTitle);
   }

   //@Test TODO: These tests are over complex, there needs to be a service layer suitable for more shallow mocking
   public void expectProductResource() {

      // Test parameters
      String pageId = "product";
      String type = "product";
      String pageTitle = "mock title";
      int expectedHttpStatusCode = HttpStatus.OK.value();
      String featureName = "ProfitAndLossFeature";
      String productName = "BasicSoleTraderProduct";
      boolean hasProductType = true;
      String regionName = ProductServiceImpl.REGION_NAME;
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriodName = "2013-04-05 (Apr13)";
      FileFormat fileFormatExcel2003 = new FileFormat();
      fileFormatExcel2003.setName("Excel 2003");
      FileFormat fileFormatExcel2007 = new FileFormat();
      fileFormatExcel2007.setName("Excel 2007");
      AvailabilityStatus availabilityStatus = new AvailabilityStatus();
      availabilityStatus.setName("released");
      String productContentItemName = "BasicSoleTraderProduct";
      float grossRetailPrice = 20f;
      String currency = "GBP";

      // Expected data
      String pageName = WordUtils.capitalize(pageId) + WordUtils.capitalize(type);
      Feature content = new Feature();
      content.setName(pageName);
      Product product = new Product();
      product.setTitle(pageTitle);
      product.setName(productName);
      ProductType productType = new ProductType();
      productType.setContentItemName(productName);
      String[] featureNames = { featureName };
      String[] bundleNames = new String[0];
      product.setFeatureNames(featureNames);
      product.setBundleNames(bundleNames);
      Feature[] features = { content };
      Product[] bundles = new Product[bundleNames.length];
      ControllerUtils controllerUtils = new ControllerUtils();
      DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(diyAccountingSpringBootConfiguration);
      Vector<Field> fields = new Vector<>();
      AvailableFormat availableFormatExcel2003 = new AvailableFormat();
      availableFormatExcel2003.setDownloadUrlFilename("mock.download.filename.html");
      availableFormatExcel2003.setFileFormat(fileFormatExcel2003);
      availableFormatExcel2003.setAvailabilityStatus(availabilityStatus);
      AvailableFormat availableFormatExcel2007 = new AvailableFormat();
      availableFormatExcel2007.setDownloadUrlFilename("mock.download.filename.html");
      availableFormatExcel2007.setFileFormat(fileFormatExcel2007);
      availableFormatExcel2007.setAvailabilityStatus(availabilityStatus);
      AvailableFormat[] availableFormats = new AvailableFormat[2];
      availableFormats[0] = availableFormatExcel2003;
      availableFormats[1] = availableFormatExcel2007;

      // Create a commercial product
      ProductTestUtils productTestUtils = new ProductTestUtils();
      CommercialProduct commercialProduct = productTestUtils.buildCommercialProduct(productContentItemName,
              productCatalogueName,
              accountingPeriodName, regionName, grossRetailPrice, currency);
      //String inventoryNumber = commercialProduct.calculateHash(commercialProduct.toString());

      // Mocks
      MockHttpServletRequest request = new MockHttpServletRequest();

      PageService pageService = EasyMock.createNiceMock(PageService.class);

      ProductService productService = EasyMock.createNiceMock(ProductService.class);
      // EasyMock.expect(productService.getFeature(featureName)).andReturn(content);
      EasyMock.expect(productService.getProduct(productName)).andReturn(product).anyTimes();
      EasyMock.expect(productService.getProductType(productName)).andReturn(productType).anyTimes();
      EasyMock.expect(productService.getFeatures(EasyMock.anyObject())).andReturn(features);
      EasyMock.expect(productService.getProducts(EasyMock.anyObject())).andReturn(bundles);
      EasyMock.expect(productService.hasProductType(product.getName())).andReturn(hasProductType).anyTimes();
      //EasyMock.expect(productService.getCommercialProductByHash(expectedToken)).andReturn(commercialProduct).anyTimes();
      EasyMock.expect(productService.getCommercialProduct(regionName, productCatalogueName, accountingPeriodName)).andReturn(commercialProduct).anyTimes();
      //EasyMock.expect(productService.getCommercialProduct(regionName, productName, accountingPeriodName)).andReturn(commercialProduct).anyTimes();
      EasyMock.expect(productService.getAvailableFormats(commercialProduct)).andReturn(availableFormats).anyTimes();

      //TranslationService translationService = EasyMock.createNiceMock(TranslationService.class);
      //EasyMock.expect(translationService.getTranslateableFields(EasyMock.anyObject())).andReturn(fields)
      //      .anyTimes();

      EasyMock.replay(pageService, productService);
      //controllerUtils.setTranslation(translationService);

      // Class under test
      ContentApi classUnderTest = new ContentApi();
      classUnderTest.setControllerUtils(controllerUtils);
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(productService);

      // Execute
      HashMap<String, Object> modelMap = classUnderTest.productForPeriod(request, productName, null);
      Assert.assertTrue(modelMap.containsKey("pages"));
      HashMap<String, Object> pagesMap = (HashMap<String, Object> )modelMap.get("pages");
      Assert.assertTrue(pagesMap.containsKey(pageId));
      Assert.assertNotNull(pagesMap.get(pageId));
      String actualPageTitle = ((Product) pagesMap.get(pageId)).getTitle();
      Assert.assertEquals(pageTitle, actualPageTitle);
      Product actualProduct = (Product) pagesMap.get("product");
      Assert.assertEquals(product, actualProduct);
   }

   /*
   //@Test // TODO: Fix or remove this test
   public void expectBuyResource() throws ContentException {

      // Test parameters
      String pageId = "buy";
      String type = "page";
      String pageTitle = "mock title";
      String productContentItemName = "BasicSoleTraderProduct";
      String featureName = "ProfitAndLossFeature";
      String randomCookieValue = UUID.randomUUID().toString();
      String imageSrc = UUID.randomUUID().toString();
      String productName = "Basic Sole Trader";
      String periodName = "2021-04-05 (Apr21)";
      String[] bundleNamePeriods = null;
      Region region = new Region();
      region.setName(ProductServiceImpl.REGION_NAME);
      float retailPrice = 20f;

      // Expected data
      String pageName = WordUtils.capitalize(pageId) + WordUtils.capitalize(type);
      Page content = new Page();
      content.setTitle(pageTitle);
      content.setName(pageName);
      Image image = new Image("<img src=\"" + imageSrc + "\" />");
      content.setImage(image);
      Product product = new Product();
      product.setTitle(pageTitle);
      product.setName(productName);
      String[] featureNames = { featureName };
      String[] bundleNames = new String[0];
      Feature[] features = new Feature[1];
      Feature feature = new Feature();
      feature.setName(featureName);
      Product[] bundles = new Product[0];
      product.setFeatureNames(featureNames);
      product.setBundleNames(bundleNames);
      ProductType productType = new ProductType();
      PrimaryFunction primaryFunction = new PrimaryFunction();
      primaryFunction.setName("Accounts");
      productType.setPrimaryFunction(primaryFunction);
      productType.setContentItemName(productContentItemName);
      ProductType[] productTypes = new ProductType[1];
      productTypes[0] = productType;
      HttpServletResponse response = new MockHttpServletResponse();
      ControllerUtils controllerUtils = new ControllerUtils();
      DiyAccountingSpringBootConfiguration appConfig = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(appConfig);
      Vector<Field> fields = new Vector<>();
      ArrayList<Cookie> cookieArray = new ArrayList<>();
      Cookie randomCookie = new Cookie(randomCookieValue, randomCookieValue);
      cookieArray.add(randomCookie);
      Cookie[] cookies = new Cookie[cookieArray.size()];
      cookieArray.toArray(cookies);
      AccountingPeriod period = new AccountingPeriod();
      period.setName(periodName);
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(region);
      productComponent.setProductType(productType);
      productComponent.setAccountingPeriod(period);
      CommercialProduct commercialProduct = new CommercialProduct();
      Bundle bundle = new Bundle();
      bundle.setRetailPrice(retailPrice);
      commercialProduct.setBundle(bundle);

      // Mock response
      PageService pageService = EasyMock.createNiceMock(PageService.class);
      EasyMock.expect(pageService.getPage(EasyMock.anyString())).andReturn(content).anyTimes();
      //EasyMock.expect(pageService.getProductPage()).andReturn(content).anyTimes();

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setCookies(cookies);

      ProductService productService = EasyMock.createNiceMock(ProductService.class);
      EasyMock.expect(productService.getBaseProducts()).andReturn(productTypes).anyTimes();
      EasyMock.expect(productService.getProduct(productName)).andReturn(product).anyTimes();
      EasyMock.expect(productService.getProduct(productContentItemName)).andReturn(product).anyTimes();
      EasyMock.expect(productService.getFeatures(featureNames)).andReturn(features).anyTimes();
      EasyMock.expect(productService.getProducts(bundleNames)).andReturn(bundles).anyTimes();
      EasyMock.expect(productService.getProductType(productName)).andReturn(productType).anyTimes();
      EasyMock.expect(productService.getAccountingPeriod(productType, periodName)).andReturn(period).anyTimes();
      EasyMock.expect(productService.buildProductComponent(region, productType, period)).andReturn(productComponent).anyTimes();
      EasyMock.expect(productService.getCommercialProduct(region, productComponent, bundleNamePeriods)).andReturn(commercialProduct).anyTimes();

      //TranslationService translationService = EasyMock.createNiceMock(TranslationService.class);
      //EasyMock.expect(translationService.getTranslateableFields(EasyMock.anyObject())).andReturn(fields)
      //      .anyTimes();

      EasyMock.replay(pageService, productService);
      //controllerUtils.setTranslation(translationService);

      // Class under test
      ContentApi classUnderTest = new ContentApi();
      classUnderTest.setControllerUtils(controllerUtils);
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(productService);

      // Execute
      // HashMap<String, Object> modelMap =
      HashMap<String, Object> modelMap = classUnderTest.productForPeriod(request, productName, periodName);

      // Check object
      Assert.assertNotNull(modelMap);

      // Check page content
      String formattedPrice = (String) modelMap.get("formattedPrice");
      Assert.assertNotNull(formattedPrice);
      ProductType selectedProduct = (ProductType) modelMap.get("selectedProduct");
      Assert.assertNotNull(selectedProduct);
      AccountingPeriod accountingPeriod = (AccountingPeriod) modelMap.get("selectedPeriod");
      Assert.assertNotNull(accountingPeriod);
      //ProductType[] availableProducts = (ProductType[]) modelMap.get("availableProducts");
      //Assert.assertNotNull(availableProducts);
      //Assert.assertTrue(availableProducts.length > 0);
      AccountingPeriod[] availablePeriods = (AccountingPeriod[]) modelMap.get("availablePeriods");
      Assert.assertTrue(modelMap.containsKey("pages"));
      HashMap<String, Object> pagesMap = (HashMap<String, Object> )modelMap.get("pages");
      Assert.assertTrue(pagesMap.containsKey(pageId));
      Assert.assertNotNull(pagesMap.get(pageId));
      //Assert.assertNotNull(availablePeriods);
      //Assert.assertEquals(0, availablePeriods.length);
      //ProductComponent[][] availableBundles = (ProductComponent[][]) modelMap.get("availableBundles");
      //Assert.assertNotNull(availableBundles);
      //Assert.assertEquals(0, availableBundles.length);

      // Expect referrer to be null
      String referrer = (String) modelMap.get("referrer");
      Assert.assertNull(referrer);
   }

   // Download an available format by name
   //@Test // TODO: Fix or remove this test
   public void expectAvailableFormatForProductByName() throws ContentException {

      // Test parameters
      String pageId = "thankyou";
      String type = "page";
      String pageTitle = "mock title";
      String productContentItemName = "BasicSoleTraderProduct";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriodName = "2013-04-05 (Apr13)";
      //String featureName = "ProfitAndLossFeature";
      String productName = "BasicSoleTraderProduct";
      boolean hasProductType = true;

      String regionName = ProductServiceImpl.REGION_NAME;
      String conversionCode = "mock-conversion-code";
      float grossRetailPrice = 20f;
      //String inventoryNumber = new HashHelper().getHash(productCalatlogueName + accountingPeriodName);
      String toEmail = "mockemail@example.com";
      String transaction = UUID.randomUUID().toString();
      String token = "mocktoken";
      String expectedEmailConfirmation = "We shall email your download link to: " + toEmail;
      String fromEmail = "mocksender@example.com";
      float vatRate = 0.2F;
      String currency = "GBP";
      String organisationName = UUID.randomUUID().toString();
      String siteName = UUID.randomUUID().toString();
      String siteUrl = UUID.randomUUID().toString();
      String vatNumber = UUID.randomUUID().toString();
      String imageSrc = UUID.randomUUID().toString();
      FileFormat fileFormatExcel2003 = new FileFormat();
      fileFormatExcel2003.setName("Excel 2003");
      FileFormat fileFormatExcel2007 = new FileFormat();
      fileFormatExcel2007.setName("Excel 2007");
      AvailabilityStatus availabilityStatus = new AvailabilityStatus();
      availabilityStatus.setName("released");

      // Expected results
      String pageName = WordUtils.capitalize(pageId) + WordUtils.capitalize(type);
      //Feature content = new Feature();
      //content.setName(pageName);
      Product product = new Product();
      product.setTitle(pageTitle);
      product.setName(productName);
      //String pageName = WordUtils.capitalize(pageId) + WordUtils.capitalize(type);
      Page content = new Page();
      content.setName(pageName);
      content.setTitle(pageTitle);
      content.setName(pageName);
      Image image = new Image("<img src=\"" + imageSrc + "\" />");
      content.setImage(image);
      AvailableFormat availableFormatExcel2003 = new AvailableFormat();
      availableFormatExcel2003.setDownloadUrlFilename("mock.download.filename.html");
      availableFormatExcel2003.setFileFormat(fileFormatExcel2003);
      availableFormatExcel2003.setAvailabilityStatus(availabilityStatus);
      AvailableFormat availableFormatExcel2007 = new AvailableFormat();
      availableFormatExcel2007.setDownloadUrlFilename("mock.download.filename.html");
      availableFormatExcel2007.setFileFormat(fileFormatExcel2007);
      availableFormatExcel2007.setAvailabilityStatus(availabilityStatus);
      AvailableFormat[] availableFormats = new AvailableFormat[2];
      availableFormats[0] = availableFormatExcel2003;
      availableFormats[1] = availableFormatExcel2007;
      ProductType productType = new ProductType();
      productType.setContentItemName(productName);
      //String[] featureNames = { featureName };
      String[] bundleNames = new String[0];
      //product.setFeatureNames(featureNames);
      product.setBundleNames(bundleNames);
      //Feature[] features = { featureContent };
      Product[] bundles = new Product[bundleNames.length];

      // Create a commercial product
      ProductTestUtils productTestUtils = new ProductTestUtils();
      CommercialProduct commercialProduct = productTestUtils.buildCommercialProduct(productContentItemName,
              productCatalogueName,
              accountingPeriodName, regionName, grossRetailPrice, currency);
      String inventoryNumber = commercialProduct.calculateHash(commercialProduct.toString());

      //Product product = new Product();
      product.setName(productContentItemName);
      product.setConversionCode(conversionCode);
      float taxAmount = (grossRetailPrice * vatRate) / (100f + vatRate);
      taxAmount = Math.round(taxAmount * 100f) / 100f;
      String productDescription = productCatalogueName + " for Year to " + accountingPeriodName;

      // Download items derived from the commercial product
      String expectedToken = commercialProduct.getHash();
      String expectedExeExcel2003 = "GB%20Accounts%20Basic%20Sole%20Trader%202013-04-05%20(Apr13)%20Excel%202003.exe";
      String expectedZipExcel2007 = "GB%20Accounts%20Basic%20Sole%20Trader%202013-04-05%20(Apr13)%20Excel%202007.zip";

      // Supporting service objects which don't need mocking
      HttpServletRequest request = new MockHttpServletRequest();
      CalculationUtils calculationUtils = new CalculationUtils();
      Site site = this.createSite(fromEmail, vatRate, organisationName, siteName, siteUrl, vatNumber);
      ControllerUtils controllerUtils = new ControllerUtils();
      DiyAccountingSpringBootConfiguration appConfig = new DiyAccountingSpringBootConfiguration();
      controllerUtils.setAppConfig(appConfig);
      Vector<Field> fields = new Vector<>();

      // Mock response
      PageService pageService = EasyMock.createNiceMock(PageService.class);
      Page[] pages = {content};
      EasyMock.expect(pageService.getAllPages()).andReturn(pages).anyTimes();
      EasyMock.expect(pageService.getPage(EasyMock.anyString())).andReturn(content).anyTimes();

      ProductService productService = EasyMock.createNiceMock(ProductService.class);
      EasyMock.expect(productService.getProduct(productName)).andReturn(product); //.anyTimes();
      EasyMock.expect(productService.getProductType(productName)).andReturn(productType).anyTimes();
      //EasyMock.expect(productService.getFeatures(EasyMock.anyObject())).andReturn(features);
      EasyMock.expect(productService.getProducts(EasyMock.anyObject())).andReturn(bundles).anyTimes();
      EasyMock.expect(productService.hasProductType(product.getName())).andReturn(hasProductType).anyTimes();
      EasyMock.expect(productService.getCommercialProductByHash(inventoryNumber)).andReturn(commercialProduct).anyTimes();
      EasyMock.expect(productService.getProduct(productContentItemName)).andReturn(product); //.anyTimes();
      EasyMock.expect(productService.getDescription(commercialProduct)).andReturn(productDescription).anyTimes();
      EasyMock.expect(productService.getCommercialProductByHash(expectedToken)).andReturn(commercialProduct).anyTimes();
      EasyMock.expect(productService.getCommercialProduct(regionName, productCatalogueName, accountingPeriodName)).andReturn(commercialProduct).anyTimes();
      EasyMock.expect(productService.getAvailableFormats(commercialProduct)).andReturn(availableFormats).anyTimes();

      //TranslationService translationService = EasyMock.createNiceMock(TranslationService.class);
      //EasyMock.expect(translationService.getTranslateableFields(EasyMock.anyObject())).andReturn(fields)
      //      .anyTimes();

      EasyMock.replay(pageService, productService);
      //controllerUtils.setTranslation(translationService);

      UrlHelper url = new UrlHelper();

      // Class under test
      ContentApi classUnderTest = new ContentApi();
      classUnderTest.setControllerUtils(controllerUtils);
      classUnderTest.setPageContent(pageService);
      classUnderTest.setProductContent(productService);
      classUnderTest.setCalculationUtils(calculationUtils);
      classUnderTest.setSite(site);
      classUnderTest.setUrl(url);

      // Execute
      //HashMap<String, Object> model = classUnderTest.download(request, productContentItemName, accountingPeriodName);
      HashMap<String, Object> model = classUnderTest.productForPeriod(request, productCatalogueName, accountingPeriodName);

      // Check
      Assert.assertNotNull(model);
      String actualToken= (String) model.get("token");
      Assert.assertEquals(expectedToken, actualToken);
      String actualExeExcel2003 = (String) model.get("exeExcel2003");
      //Assert.assertEquals(expectedExeExcel2003, actualExeExcel2003);
      String actualZipExcel2007 = (String) model.get("zipExcel2007");
      //Assert.assertEquals(expectedZipExcel2007, actualZipExcel2007);
   }
*/

   private Site createSite(String fromEmail, float vatRate, String organisationName, String siteName, String siteUrl,
                           String vatNumber) {
      Site site = new Site();
      site.setEmail(fromEmail);
      site.setVat(vatRate);
      site.setOrganisation(organisationName);
      site.setName(siteName);
      site.setUrl(siteUrl);
      site.setVatNumber(vatNumber);
      return site;
   }
}
