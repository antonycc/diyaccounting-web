package uk.co.diyaccounting.web.unit;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.diyaccounting.web.catalogue.*;
import uk.co.diyaccounting.web.service.CatalogueServiceMemImpl;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatalogueServiceMemImplTest {

   private FileLikePathService file = new FileLikePathService();

   private CatalogueServiceMemImpl catalogue = new CatalogueServiceMemImpl();

   //private static String catalogueClasspathLocation = "target/test-classes";
   private static final String localPath = CatalogueServiceMemImpl.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static String catalogueResource = "urn:diyaccounting.co.uk:file://" + localPath + "test-catalogue/catalogue.txt";
   private static String catalogueNamesResource = "urn:diyaccounting.co.uk:file://" + localPath + "test-catalogue/catalogueNameContentItemName.properties";
   private static String catalogueBundlePriceResource = "urn:diyaccounting.co.uk:file://" + localPath + "test-catalogue/catalogueBundlePrices.properties";
   private static String stockLocation = "target/classes/test-stock/";
   private static String stockPath = "urn:diyaccounting.co.uk:file://" + stockLocation;

   @Before
   public void ingestCatalogue() throws IOException, ParseException {
      this.catalogue.configureCatalogue(this.catalogueBundlePriceResource, this.catalogueNamesResource, this.catalogueResource,
            this.stockPath);
      this.catalogue.populateCatalogue(this.file);
   }


   @Test
   public void expectArrayOfProductTypes() {

      // Test parameters
      String regionName = "GB";
      String productTypeCatalogueName = "Basic Sole Trader";
      String productTypeContentItemName = "BasicSoleTraderProduct";

      // Expected Results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productTypeCatalogueName);
      productType.setContentItemName(productTypeContentItemName);
      List<ProductType> productTypes = new ArrayList<>();
      productTypes.add(productType);

      // Mock persistence layer

      // Class under test
      CatalogueServiceMemImpl classUnderTest = this.catalogue;

      // Execute tests
      ProductType[] actualProductTypes = classUnderTest.getProductTypes(regionName);
      Assert.assertTrue("actualProductTypes should not be empty", ArrayUtils.isNotEmpty(actualProductTypes));
      Assert.assertTrue(new ArrayList<>(Arrays.asList(actualProductTypes)).stream()
            .anyMatch(actualProductType -> actualProductType.getCatalogueName().equals(productTypeCatalogueName))
            );
   }

   @Test
   public void expectArrayOfAccountingPeriods() {

      // Test parameters
      String regionName = "GB";
      String accountingPeriodName = "2018-04-05 (Apr18)";
      String productTypeCatalogueName = "Basic Sole Trader";
      String productTypeContentItemName = "BasicSoleTraderProduct";

      // Expected Results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productTypeCatalogueName);
      productType.setContentItemName(productTypeContentItemName);
      AccountingPeriod accountingPeriod = new AccountingPeriod();
      accountingPeriod.setName(accountingPeriodName);
      List<AccountingPeriod> accountingPeriods = new ArrayList<AccountingPeriod>();
      accountingPeriods.add(accountingPeriod);

      // Mock persistence layer

      // Class under test
      CatalogueServiceMemImpl classUnderTest = this.catalogue;

      // Execute tests
      AccountingPeriod[] actualAccountingPeriods = classUnderTest.getAccountingPeriods(productTypeCatalogueName,
               regionName);
      Assert.assertEquals(accountingPeriods.get(0).toString(), actualAccountingPeriods[0].toString());
   }

   @Test
   public void expectCommercialProduct() {

      // Test parameters
      String regionName = "GB";
      String accountingPeriodNameOne = "2018-04-05 (Apr18)";
      String productTypeCatalogueName = "Basic Sole Trader";
      String productTypeContentItemName = "BasicSoleTraderProduct";

      // Expected Results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productTypeCatalogueName);
      productType.setContentItemName(productTypeContentItemName);
      AccountingPeriod accountingPeriodOne = new AccountingPeriod();
      accountingPeriodOne.setName(accountingPeriodNameOne);
      ProductComponent productComponentOne = new ProductComponent();
      productComponentOne.setRegion(region);
      productComponentOne.setProductType(productType);
      productComponentOne.setAccountingPeriod(accountingPeriodOne);
      CommercialProduct commercialProductOne = new CommercialProduct();
      Bundle bundleOne = new Bundle();
      commercialProductOne.setBundle(bundleOne);
      commercialProductOne.setProductComponent(productComponentOne);
      ProductComponent[] bundledProductComponents = new ProductComponent[0];

      // Mock persistence layer

      // Class under test
      CatalogueServiceMemImpl classUnderTest = this.catalogue;

      // Execute tests
      CommercialProduct actualCommercialProduct = classUnderTest.getCommercialProduct(productComponentOne,
               bundledProductComponents);
      Assert.assertEquals(commercialProductOne.toString(), actualCommercialProduct.toString());
   }

   @Test
   public void expectCommercialProductByHash() {

      // Test parameters
      String regionName = "GB";
      String accountingPeriodNameOne = "2018-04-05 (Apr18)";
      String productTypeCatalogueName = "Basic Sole Trader";
      String productTypeContentItemName = "BasicSoleTraderProduct";

      // Expected Results
      Region region = new Region();
      region.setName(regionName);
      ProductType productType = new ProductType();
      productType.setCatalogueName(productTypeCatalogueName);
      productType.setContentItemName(productTypeContentItemName);
      AccountingPeriod accountingPeriodOne = new AccountingPeriod();
      accountingPeriodOne.setName(accountingPeriodNameOne);
      ProductComponent productComponentOne = new ProductComponent();
      productComponentOne.setRegion(region);
      productComponentOne.setProductType(productType);
      productComponentOne.setAccountingPeriod(accountingPeriodOne);
      CommercialProduct commercialProductOne = new CommercialProduct();
      Bundle bundleOne = new Bundle();
      commercialProductOne.setBundle(bundleOne);
      commercialProductOne.setProductComponent(productComponentOne);
      String hash = commercialProductOne.getHash();

      // Mock persistence layer

      // Class under test
      CatalogueServiceMemImpl classUnderTest = this.catalogue;

      // Execute tests
      CommercialProduct actualCommercialProduct = classUnderTest.getCommercialProductByHash(hash);
      Assert.assertEquals(commercialProductOne.toString(), actualCommercialProduct.toString());
   }

   @Test
   public void expectToBuildCommercialProductFromNamedComponents() {

      // Test parameters
      String regionName = "GB";
      String productCatalogueName = "Basic Sole Trader";
      String accountingPeriodName = "2018-04-05 (Apr18)";
      float expectedPrice = 20f;
      String expectedCurrency = "GBP";

      // Expected Results

      // Mocks

      // Class under test
      CatalogueServiceMemImpl classUnderTest = this.catalogue;

      // Execution
      CommercialProduct commercialProduct = classUnderTest.buildCommercialProduct(regionName, productCatalogueName, accountingPeriodName);

      // Checks
      Assert.assertNotNull(commercialProduct);
      Assert.assertEquals(regionName, commercialProduct.getProductComponent().getRegion().getName());
      Assert.assertEquals(productCatalogueName, commercialProduct.getProductComponent().getProductType().getCatalogueName());
      Assert.assertEquals(accountingPeriodName, commercialProduct.getProductComponent().getAccountingPeriod().getName());
      Assert.assertNotNull(commercialProduct.getBundle());
      Assert.assertTrue(expectedPrice == commercialProduct.getBundle().getRetailPrice());
      Assert.assertTrue(expectedCurrency.equals(commercialProduct.getBundle().getRetailPriceCurrencyCode()));
   }
}