package uk.co.diyaccounting.web.ops;

import uk.co.diyaccounting.web.catalogue.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Tests utilities for products
 * 
 * @author antony
 */
public class ProductTestUtils {

   /**
    * Build a commercial product from raw information
    * 
    * @param productContentItemName name of this item in the CMS
    * @param productCalatlogueName name of this item in the catalogue
    * @param accountingPeriodName accounting period as used in the catalogue
    * @param regionName region name as used in the catalogue
    * @param grossRetailPrice retail price inclusive of vat
    * @return a populated commercial product
    */
   public CommercialProduct buildCommercialProduct(final String productContentItemName,
            final String productCalatlogueName,
            final String accountingPeriodName, final String regionName,
            final float grossRetailPrice, final String currency) {
      PrimaryFunction primaryFunction = new PrimaryFunction();
      primaryFunction.setName("Accounts");
      ProductType productType = new ProductType();
      productType.setPrimaryFunction(primaryFunction);
      productType.setContentItemName(productContentItemName);
      productType.setCatalogueName(productCalatlogueName);
      ProductType[] productTypes = new ProductType[1];
      productTypes[0] = productType;
      AccountingPeriod accountingPeriod = new AccountingPeriod();
      accountingPeriod.setName(accountingPeriodName);
      AccountingPeriod[] accountingPeriods = new AccountingPeriod[1];
      accountingPeriods[0] = accountingPeriod;
      Region region = new Region();
      region.setName(regionName);
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(region);
      productComponent.setAccountingPeriod(accountingPeriod);
      productComponent.setProductType(productType);
      CommercialProduct commercialProduct = new CommercialProduct();
      //commercialProduct.setHash(inventoryNumber);
      Bundle bundle = new Bundle();
      bundle.setRetailPrice(grossRetailPrice);
      bundle.setRetailPriceCurrencyCode(currency);
      Set<ProductType> productTypeSet = new TreeSet<ProductType>();
      productTypeSet.add(productType);
      bundle.setProductTypes(productTypeSet);
      commercialProduct.setBundle(bundle);
      //List<ProductComponent> bundledProductComponents = new ArrayList<ProductComponent>();
      //commercialProduct.setBundledProductComponents(bundledProductComponents);
      commercialProduct.setProductComponent(productComponent);
      return commercialProduct;
   }
}
