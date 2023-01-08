package uk.co.diyaccounting.web.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.web.catalogue.*;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Product;
import uk.co.diyaccounting.web.service.*;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.text.ParseException;
import java.util.UUID;

//import uk.co.diyaccounting.content.service.AnchorBuilder;
//import uk.co.diyaccounting.content.service.AnchorBuilderImpl;

/**
 * Tests for the product service mocking the content service where necessary
 * 
 * @author antony
 */
public class ProductServiceTest {

   private static final String localPath = MdPageTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";
   private static String baseCataloguePath = baseFileUrn;
   private static String catalogueResource = baseCataloguePath + "catalogue/catalogue.txt";
   private static String catalogueNamesResource = baseCataloguePath + "catalogue/catalogueNameContentItemName.properties";
   private static String catalogueBundlePriceResource = baseCataloguePath + "catalogue/catalogueBundlePrices.properties";
   private static String productContentBasePath = baseFileUrn + "test-content/";

   @Test
   public void expectIngestWithoutError() throws ContentException {

      // Test parameters

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      ////classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setCatalogueResource(catalogueResource);
      classUnderTest.setCatalogueNamesResource(catalogueNamesResource);
      classUnderTest.setCatalogueBundlePriceResource(catalogueBundlePriceResource);

      // Execute
      classUnderTest.populateCatalogue();
   }

   @Test
   public void expectAllProductTypesToHaveContent() throws ContentException, ParseException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productFilter = "Product.md";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product3ContentItemName = "no.such.contentitem";
      String product4ContentItemName = "no.such.contentitem.again";
      String product1CatalogueName = "Basic Sole Trader";
      String product2CatalogueName = "Self Employed";
      String product4CatalogueName = "No Such Product";
      String region = ProductServiceImpl.REGION_NAME;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      ProductType product2Type = new ProductType();
      product2Type.setCatalogueName(product2CatalogueName);
      product2Type.setContentItemName(product2ContentItemName);
      ProductType product4Type = new ProductType();
      product4Type.setCatalogueName(product4CatalogueName);
      product4Type.setContentItemName(product4ContentItemName);
      ProductType[] allProductTypes = new ProductType[3];
      allProductTypes[0] = product1Type;
      allProductTypes[1] = product2Type;
      allProductTypes[2] = product4Type;
      String[] productContentItemNames = {product2ContentItemName, product1ContentItemName, product3ContentItemName};
      String product1Path = productContentBasePath + product1ContentItemName + ".md";
      String product2Path = productContentBasePath + product2ContentItemName + ".md";
      String product3Path = productContentBasePath + product3ContentItemName + ".md";
      //ContentList products = new ContentList();
      //products.setContentList(productContentItemNames);
      String[] products = {product1ContentItemName, product2ContentItemName, product3ContentItemName};
      Product product1 = new Product();
      product1.setName(product1ContentItemName);
      //product1.setHeadlineProductName(product1ContentItemName);
      Product product2 = new Product();
      product2.setName(product2ContentItemName);
      //product2.setHeadlineProductName(product2ContentItemName);
      Product product3 = new Product();
      product3.setName(product3ContentItemName);
      //product3.setHeadlineProductName("unmatched");
      ProductType[] expectedProductTypes = new ProductType[2];
      expectedProductTypes[0] = product1Type;
      expectedProductTypes[1] = product2Type;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      EasyMock.expect(contentService.getProductNames(productContentBasePath)).andReturn(products).anyTimes();
      EasyMock.expect(contentService.getProduct(product1Path)).andReturn(product1).anyTimes();
      EasyMock.expect(contentService.getProduct(product2Path)).andReturn(product2).anyTimes();
      EasyMock.expect(contentService.getProduct(product3Path)).andReturn(product3).anyTimes();

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes).anyTimes();
      //EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes).anyTimes();

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      ProductType[] actualProductTypes = classUnderTest.getBaseProducts();

      // Check
      Assert.assertEquals(expectedProductTypes.length, actualProductTypes.length);
   }

   @Test
   public void expectAllProducts() throws ContentException, ParseException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product3ContentItemName = "no.such.contentitem";
      String productFilter = "Product.md";

      // Expected results
      String[] productContentItemNames = {product2ContentItemName, product1ContentItemName, product3ContentItemName};
      String product1Path = productContentBasePath + product1ContentItemName + ".md";
      String product2Path = productContentBasePath + product2ContentItemName + ".md";
      String product3Path = productContentBasePath + product3ContentItemName + ".md";
      //ContentList products = new ContentList();
      //products.setContentList(productContentItemNames);
      String[] products = {product1ContentItemName, product2ContentItemName, product3ContentItemName};
      Product product1 = new Product();
      product1.setName(product1ContentItemName);
      //product1.setHeadlineProductName(product1ContentItemName);
      Product product2 = new Product();
      product2.setName(product2ContentItemName);
      //product2.setHeadlineProductName(product2ContentItemName);
      Product product3 = new Product();
      product3.setName(product3ContentItemName);
      //product3.setHeadlineProductName("unmatched");
      Product[] expectedProducts = new Product[3];
      expectedProducts[0] = product1;
      expectedProducts[1] = product2;
      expectedProducts[2] = product3;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      EasyMock.expect(contentService.getProductNames(productContentBasePath)).andReturn(products).anyTimes();
      EasyMock.expect(contentService.getProduct(product1Path)).andReturn(product1);
      EasyMock.expect(contentService.getProduct(product2Path)).andReturn(product2);
      EasyMock.expect(contentService.getProduct(product3Path)).andReturn(product3);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      Product[] actualProductTypes = classUnderTest.getAllProductsIncludingThoseNotInCatalogue();

      // Check
      Assert.assertEquals(expectedProducts.length, actualProductTypes.length);
   }

   @Test
   public void expectProductTypeMatchingContentItemName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product1CatalogueName = "Basic Sole Trader";
      String product2CatalogueName = "Self Employed";
      String region = ProductServiceImpl.REGION_NAME;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      ProductType product2Type = new ProductType();
      product2Type.setCatalogueName(product2CatalogueName);
      product2Type.setContentItemName(product2ContentItemName);
      ProductType[] allProductTypes = new ProductType[2];
      allProductTypes[0] = product1Type;
      allProductTypes[1] = product2Type;

	   // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      ProductType actualProductType = classUnderTest.getProductType(product1ContentItemName);
      Assert.assertEquals(product1Type, actualProductType);
   }

   @Test
   public void expectProductTypeMatchingCatalogueName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product1CatalogueName = "Basic Sole Trader";
      String product2CatalogueName = "Self Employed";
      String region = ProductServiceImpl.REGION_NAME;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      ProductType product2Type = new ProductType();
      product2Type.setCatalogueName(product2CatalogueName);
      product2Type.setContentItemName(product2ContentItemName);
      ProductType[] allProductTypes = new ProductType[2];
      allProductTypes[0] = product1Type;
      allProductTypes[1] = product2Type;

	   // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      ProductType actualProductType = classUnderTest.getProductType(product1CatalogueName);
      Assert.assertEquals(product1Type, actualProductType);
   }

   @Test(expected = ContentException.class)
   public void expectNoMatchingProductType() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product1CatalogueName = "Basic Sole Trader";
      String product2CatalogueName = "Self Employed";
      String region = ProductServiceImpl.REGION_NAME;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      ProductType product2Type = new ProductType();
      product2Type.setCatalogueName(product2CatalogueName);
      product2Type.setContentItemName(product2ContentItemName);
      ProductType[] allProductTypes = new ProductType[2];
      allProductTypes[0] = product1Type;
      allProductTypes[1] = product2Type;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      classUnderTest.getProductType("No such product type");
   }

   @Test
   public void expectHasProductTypeMatchingContentItemName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product1CatalogueName = "Basic Sole Trader";
      String product2CatalogueName = "Self Employed";
      String region = ProductServiceImpl.REGION_NAME;
      boolean expectedHasProductType = true;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      ProductType product2Type = new ProductType();
      product2Type.setCatalogueName(product2CatalogueName);
      product2Type.setContentItemName(product2ContentItemName);
      ProductType[] allProductTypes = new ProductType[2];
      allProductTypes[0] = product1Type;
      allProductTypes[1] = product2Type;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      boolean actualHasProductType = classUnderTest.hasProductType(product1ContentItemName);
      Assert.assertEquals(expectedHasProductType, actualHasProductType);
   }

   @Test
   public void expectHasProductTypeMatchingCatalogueName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product1CatalogueName = "Basic Sole Trader";
      String product2CatalogueName = "Self Employed";
      String region = ProductServiceImpl.REGION_NAME;
      boolean expectedHasProductType = true;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      ProductType product2Type = new ProductType();
      product2Type.setCatalogueName(product2CatalogueName);
      product2Type.setContentItemName(product2ContentItemName);
      ProductType[] allProductTypes = new ProductType[2];
      allProductTypes[0] = product1Type;
      allProductTypes[1] = product2Type;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      boolean actualHasProductType = classUnderTest.hasProductType(product1CatalogueName);
      Assert.assertEquals(expectedHasProductType, actualHasProductType);
   }

   @Test
   public void expectHasNotMatchingProductType() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product2ContentItemName = "SelfEmployedProduct";
      String product1CatalogueName = "Basic Sole Trader";
      String product2CatalogueName = "Self Employed";
      String region = ProductServiceImpl.REGION_NAME;
      boolean expectedHasProductType = false;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      ProductType product2Type = new ProductType();
      product2Type.setCatalogueName(product2CatalogueName);
      product2Type.setContentItemName(product2ContentItemName);
      ProductType[] allProductTypes = new ProductType[2];
      allProductTypes[0] = product1Type;
      allProductTypes[1] = product2Type;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(region)).andReturn(allProductTypes);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      boolean actualHasProductType = classUnderTest.hasProductType("No such product type");
      Assert.assertEquals(expectedHasProductType, actualHasProductType);
   }

   @Test
   public void expectTwoAccountingPeriods() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String product1ContentItemName = "BasicSoleTraderProduct";
      String product1CatalogueName = "Basic Sole Trader";
      String region = ProductServiceImpl.REGION_NAME;

      // Expected results
      ProductType product1Type = new ProductType();
      product1Type.setCatalogueName(product1CatalogueName);
      product1Type.setContentItemName(product1ContentItemName);
      AccountingPeriod accountingPeriod1 = new AccountingPeriod();
      AccountingPeriod accountingPeriod2 = new AccountingPeriod();
      AccountingPeriod[] expectedAccountingPeriods = new AccountingPeriod[2];
      expectedAccountingPeriods[0] = accountingPeriod1;
      expectedAccountingPeriods[1] = accountingPeriod2;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getAvailableAccountingPeriods(product1Type.getCatalogueName(),
               region)).andReturn(expectedAccountingPeriods);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      AccountingPeriod[] actualAccountingPeriods = classUnderTest.getAccountingPeriods(product1Type);
      Assert.assertEquals(1, actualAccountingPeriods.length);
   }

   @Test
   public void expectAccountingPeriodForProductTypeAndPeriodName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productContentItemName = "BasicSoleTraderProduct";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriod1Name = "2008-04-05 (Apr08)";
      String accountingPeriod2Name = "2008-04-05 (Apr08)";
      String region = ProductServiceImpl.REGION_NAME;

      // Expected results
      ProductType productType = new ProductType();
      productType.setCatalogueName(productCatalogueName);
      productType.setContentItemName(productContentItemName);
      AccountingPeriod accountingPeriod1 = new AccountingPeriod();
      accountingPeriod1.setName(accountingPeriod1Name);
      AccountingPeriod accountingPeriod2 = new AccountingPeriod();
      accountingPeriod2.setName(accountingPeriod2Name);
      AccountingPeriod[] accountingPeriods = new AccountingPeriod[2];
      accountingPeriods[0] = accountingPeriod1;
      accountingPeriods[1] = accountingPeriod2;

	   // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(
               catalogueService.getAccountingPeriods(productType.getCatalogueName(), region))
               .andReturn(
                        accountingPeriods);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      AccountingPeriod actualAccountingPeriod = classUnderTest.getAccountingPeriod(productType, accountingPeriod1Name);
      Assert.assertEquals(accountingPeriod1.getName(), actualAccountingPeriod.getName());
   }

   @Test(expected = ContentException.class)
   public void expectNoAccountingPeriodForProductTypeAndPeriodName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productContentItemName = "BasicSoleTraderProduct";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriod1Name = "2008-04-05 (Apr08)";
      String accountingPeriod2Name = "2008-04-05 (Apr08)";
      String accountingPeriodName = "no.such.name";
      String region = ProductServiceImpl.REGION_NAME;

      // Expected results
      ProductType productType = new ProductType();
      productType.setCatalogueName(productCatalogueName);
      productType.setContentItemName(productContentItemName);
      AccountingPeriod accountingPeriod1 = new AccountingPeriod();
      accountingPeriod1.setName(accountingPeriod1Name);
      AccountingPeriod accountingPeriod2 = new AccountingPeriod();
      accountingPeriod2.setName(accountingPeriod2Name);
      AccountingPeriod[] accountingPeriods = new AccountingPeriod[2];
      accountingPeriods[0] = accountingPeriod1;
      accountingPeriods[1] = accountingPeriod2;

	   // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);

      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(
               catalogueService.getAccountingPeriods(productType.getCatalogueName(), region))
               .andReturn(
                        accountingPeriods);

      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);

      FileLikePathService fileLikePathService = new FileLikePathService();

      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      AccountingPeriod actualAccountingPeriod = classUnderTest.getAccountingPeriod(productType, accountingPeriodName);
      Assert.assertEquals(accountingPeriod1.getName(), actualAccountingPeriod.getName());
   }

   @Test
   public void expectProductComponent() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productContentItemName = "BasicSoleTraderProduct";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriod1Name = "2008-04-05 (Apr08)";
      String regionName = ProductServiceImpl.REGION_NAME;

      // Expected results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productCatalogueName);
      productType.setContentItemName(productContentItemName);
      AccountingPeriod accountingPeriod1 = new AccountingPeriod();
      accountingPeriod1.setName(accountingPeriod1Name);

	   // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      ProductComponent actualProductComponent = classUnderTest.buildProductComponent(region, productType,
               accountingPeriod1);
      Assert.assertEquals(accountingPeriod1.getName(), actualProductComponent.getAccountingPeriod().getName());
      Assert.assertEquals(productType.getCatalogueName(), actualProductComponent.getProductType()
               .getCatalogueName());
   }

   @Test
   public void expectProductComponents() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productContentItemName = "BasicSoleTraderProduct";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriod1Name = "2008-04-05 (Apr08)";
      String regionName = ProductServiceImpl.REGION_NAME;

      // Expected results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productCatalogueName);
      productType.setContentItemName(productContentItemName);
      AccountingPeriod accountingPeriod1 = new AccountingPeriod();
      accountingPeriod1.setName(accountingPeriod1Name);
	   ProductComponent[][] expectedProductComponentsList = new ProductComponent[1][1];
      ProductComponent productComponent = new ProductComponent();
      productComponent.setProductType(productType);
      productComponent.setAccountingPeriod(accountingPeriod1);
      productComponent.setRegion(region);
      expectedProductComponentsList[0][0] = productComponent;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getBundledProductComponents(productComponent)).andReturn(
               expectedProductComponentsList);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      ProductComponent[][] actualProductComponentList = classUnderTest.getBundledProductComponents(productComponent);
      Assert.assertEquals(accountingPeriod1.getName(), actualProductComponentList[0][0].getAccountingPeriod()
               .getName());
      Assert.assertEquals(productType.getCatalogueName(), actualProductComponentList[0][0].getProductType()
               .getCatalogueName());
   }

   @Test
   public void expectCommercialProductForProductComponentAndBundleNamePeriods() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productCatalogueName = "Basic Sole Trader";
      String productContentItemName = "BasicSoleTraderProduct";
      String accountingPeriodName = "2013-04-05 (Apr13)";
      String regionName = ProductServiceImpl.REGION_NAME;
      String[] bundleNamePeriods = {productContentItemName + "&" + accountingPeriodName};

      // Expected results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productCatalogueName);
      productType.setContentItemName(productContentItemName);
      AccountingPeriod accountingPeriod = new AccountingPeriod();
      accountingPeriod.setName(accountingPeriodName);
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(region);
      productComponent.setProductType(productType);
      productComponent.setAccountingPeriod(accountingPeriod);
      Bundle bundle = new Bundle();
      CommercialProduct commercialProduct = new CommercialProduct();
      commercialProduct.setBundle(bundle);
      commercialProduct.setProductComponent(productComponent);
      AccountingPeriod[] accountingPeriods = new AccountingPeriod[1];
      accountingPeriods[0] = accountingPeriod;
      ProductType[] productTypes = new ProductType[1];
      productTypes[0] = productType;
      ProductComponent[] bundledProductComponents = new ProductComponent[1];
      bundledProductComponents[0] = productComponent;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getProductTypes(regionName)).andReturn(productTypes);
      EasyMock.expect(catalogueService.getAccountingPeriods(productType.getCatalogueName(), regionName)).andReturn(
               accountingPeriods);
      EasyMock.expect(
               catalogueService.getCommercialProduct((ProductComponent) EasyMock.anyObject(),
                        (ProductComponent[]) EasyMock.anyObject())).andReturn(
               commercialProduct);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      CommercialProduct actualCommercialProduct = classUnderTest.getCommercialProduct(region, productComponent,
               bundleNamePeriods);
      Assert.assertNotNull(actualCommercialProduct);
      Assert.assertEquals(accountingPeriod.getName(), actualCommercialProduct.getProductComponent()
               .getAccountingPeriod().getName());
      Assert.assertEquals(productType.getCatalogueName(), actualCommercialProduct.getProductComponent()
               .getProductType()
               .getCatalogueName());
   }

   @Test
   public void expectCommercialProductByHash() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriodName = "no.such.name";
      String regionName = ProductServiceImpl.REGION_NAME;
      String commercialProductId = UUID.randomUUID().toString();

      // Expected results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productCatalogueName);
      AccountingPeriod accountingPeriod = new AccountingPeriod();
      accountingPeriod.setName(accountingPeriodName);
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(region);
      productComponent.setProductType(productType);
      productComponent.setAccountingPeriod(accountingPeriod);
      Bundle bundle = new Bundle();
      CommercialProduct commercialProduct = new CommercialProduct();
      commercialProduct.setBundle(bundle);
      commercialProduct.setProductComponent(productComponent);
	   String expectedDescription = productCatalogueName + " for Year to " + accountingPeriodName;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.getCommercialProductByHash(commercialProductId)).andReturn(commercialProduct);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);
	   //AnchorBuilder anchor = new AnchorBuilderImpl();

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
	   //classUnderTest.setAnchorService(anchor);
	   classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      CommercialProduct actualCommercialProduct = classUnderTest.getCommercialProductByHash(commercialProductId);
      Assert.assertEquals(accountingPeriod.getName(), actualCommercialProduct.getProductComponent()
               .getAccountingPeriod().getName());
      Assert.assertEquals(productType.getCatalogueName(), actualCommercialProduct.getProductComponent()
               .getProductType()
               .getCatalogueName());
	   String actualDescription = classUnderTest.getDescription(commercialProduct);
	   Assert.assertEquals(expectedDescription, actualDescription);
   }

   @Test
   public void expectCommercialProduct() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriodName = "no.such.name";
      String regionName = ProductServiceImpl.REGION_NAME;

      // Expected results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productCatalogueName);
      AccountingPeriod accountingPeriod = new AccountingPeriod();
      accountingPeriod.setName(accountingPeriodName);
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(region);
      productComponent.setProductType(productType);
      productComponent.setAccountingPeriod(accountingPeriod);
      Bundle bundle = new Bundle();
      CommercialProduct commercialProduct = new CommercialProduct();
      commercialProduct.setBundle(bundle);
      commercialProduct.setProductComponent(productComponent);
      String expectedDescription = productCatalogueName + " for Year to " + accountingPeriodName;

      // Mocks
      ContentService contentService = EasyMock.createNiceMock(ContentService.class);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      EasyMock.expect(catalogueService.buildCommercialProduct(regionName, productCatalogueName, accountingPeriodName)).andReturn(commercialProduct);
      EasyMock.expect(catalogueService.getCommercialProductByHash(commercialProduct.getHash())).andReturn(commercialProduct);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);
      //AnchorBuilder anchor = new AnchorBuilderImpl();

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      //classUnderTest.setAnchorService(anchor);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      CommercialProduct actualCommercialProduct = classUnderTest.getCommercialProduct(regionName, productCatalogueName, accountingPeriodName);
      Assert.assertNotNull(actualCommercialProduct);
      Assert.assertEquals(accountingPeriod.getName(), actualCommercialProduct.getProductComponent()
            .getAccountingPeriod().getName());
      Assert.assertEquals(productType.getCatalogueName(), actualCommercialProduct.getProductComponent()
            .getProductType()
            .getCatalogueName());
      String actualDescription = classUnderTest.getDescription(commercialProduct);
      Assert.assertEquals(expectedDescription, actualDescription);
   }

   @Test
   public void expectProductFromContentItemName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String contentItemName = "BasicSoleTraderProduct";
      String prefix = productContentBasePath;
	   String expectedTitle = "mock title";
      Product content = new Product();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getProduct(prefix + contentItemName + ".md")).andReturn(content);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      Product actualContent = classUnderTest.getProduct(contentItemName);
      Assert.assertEquals(expectedTitle, actualContent.getTitle());
   }

   @Test
   public void expectFeatureFromContentItemName() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String contentItemName = "ProfitAndLossFeature";
      String prefix = productContentBasePath;
	   String expectedTitle = "mock title";
      Feature content = new Feature();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getFeature(prefix + contentItemName + ".md")).andReturn(content);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      Feature actualContent = classUnderTest.getFeature(contentItemName);
      Assert.assertEquals(expectedTitle, actualContent.getTitle());
   }

   @Test
   public void expectProductsFromContentItemNames() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String contentItemName = "BasicSoleTraderProduct";
      String[] contentItemNames = {contentItemName};
      String prefix = productContentBasePath;
	   String expectedTitle = "mock title";
      Product content = new Product();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getProduct(prefix + contentItemName + ".md")).andReturn(content);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      Product[] actualContent = classUnderTest.getProducts(contentItemNames);
      Assert.assertEquals(expectedTitle, actualContent[0].getTitle());
   }

   @Test
   public void expectFeaturesFromContentItemNames() throws ContentException {

      // Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
      String contentItemName = "ProfitAndLossFeature";
      String[] contentItemNames = {contentItemName};
      String prefix = productContentBasePath;
	   String expectedTitle = "mock title";
      Feature content = new Feature();
      content.setTitle(expectedTitle);

      // Mocks
      ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
      EasyMock.expect(contentService.getFeature(prefix + contentItemName + ".md")).andReturn(content);
      CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
      IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
      FileLikePathService fileLikePathService = new FileLikePathService();
      EasyMock.replay(contentService, catalogueService, ingestService);

      // Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

      // Execute
      Feature[] actualContent = classUnderTest.getFeatures(contentItemNames);
      Assert.assertEquals(expectedTitle, actualContent[0].getTitle());
   }

	@Test
	public void expectAvailableFormatsToBePassedThrough(){

		// Test parameters
      String productContentBasePath = baseFileUrn + "test-content/";
		String downloadUrlFielname = "mock.download.filename.md";
		AvailableFormat availableFormat = new AvailableFormat();
		availableFormat.setDownloadUrlFilename(downloadUrlFielname);
		AvailableFormat[] availableFormats = new AvailableFormat[1];
		availableFormats[0] = availableFormat;

		// Expected results

		// Mocks
		ContentServiceImpl contentService = EasyMock.createNiceMock(ContentServiceImpl.class);
		CatalogueService catalogueService = EasyMock.createNiceMock(CatalogueService.class);
		EasyMock.expect(catalogueService.getAvailableFormatsForCommercialProduct(null))
			.andReturn(availableFormats);
		IngestService ingestService = EasyMock.createNiceMock(IngestService.class);
		FileLikePathService fileLikePathService = new FileLikePathService();
		EasyMock.replay(contentService, catalogueService, ingestService);

		// Class under test
      ProductServiceImpl classUnderTest = new ProductServiceImpl();
      classUnderTest.setContent(contentService);
      classUnderTest.setCatalogue(catalogueService);
      //classUnderTest.setIngest(ingestService);
      classUnderTest.setFileLikePathService(fileLikePathService);
      classUnderTest.setProductContentBasePath(productContentBasePath);

		// Execute
		AvailableFormat[] actualAvailableFormats = classUnderTest.getAvailableFormats(null);

		// Checks
		Assert.assertEquals(
			availableFormats[0].getDownloadUrlFilename(),
			actualAvailableFormats[0].getDownloadUrlFilename());
	}
}
