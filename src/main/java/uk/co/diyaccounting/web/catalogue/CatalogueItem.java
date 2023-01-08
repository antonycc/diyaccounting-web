package uk.co.diyaccounting.web.catalogue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Populated from catalogue.txt
 *
 * Package,Platform,Status,Legacy
 * GB Accounts Basic Sole Trader 2017-04-05 (Apr17),Excel 2003,released,unused
 */
public class CatalogueItem  {
   public String regionName;
   public String primaryFunctionName;
   public String productTypeCatalogueName;
   public String accountingPeriodName;
   public ArrayList<String> fileFormatNames = new ArrayList<>();
   public String availabilityStatusName;
   public String contentItemName;
   public float retailPrice;
   public String retailPriceCurrencyCode;

   public Region toRegion(){
      Region region = new Region();
      region.setName(this.regionName);
      return region;
   }

   public PrimaryFunction toPrimaryFunction(){
      PrimaryFunction primaryFunction = new PrimaryFunction();
      primaryFunction.setName(this.primaryFunctionName);
      return primaryFunction;
   }

   public ProductType toProductType(){
      ProductType productType = new ProductType();
      PrimaryFunction primaryFunction = new PrimaryFunction();
      primaryFunction.setName(this.primaryFunctionName);
      productType.setPrimaryFunction(primaryFunction);
      productType.setCatalogueName(this.productTypeCatalogueName);
      productType.setContentItemName(this.contentItemName);
      return productType;
   }

   public AccountingPeriod toAccountingPeriod(){
      AccountingPeriod accountingPeriod = new AccountingPeriod();
      accountingPeriod.setName(this.accountingPeriodName);
      return accountingPeriod;
   }

   public List<FileFormat> toFileFormatList(){
      return this.fileFormatNames.stream()
            .map(fileFormatName -> this.toFileFormat(fileFormatName))
            .collect(Collectors.toList());
   }

   public FileFormat toFileFormat(String fileFormatName){
      FileFormat fileFormat = new FileFormat();
      fileFormat.setName(fileFormatName);
      return fileFormat;
   }

   public AvailabilityStatus toAvailabilityStatus(){
      AvailabilityStatus availabilityStatus = new AvailabilityStatus();
      availabilityStatus.setName(this.availabilityStatusName);
      return availabilityStatus;
   }

   public Bundle toBundle() {
      Bundle bundle = new Bundle();
      bundle.setCatalogueNames(this.productTypeCatalogueName);
      HashSet<ProductType> productTypes = new HashSet<>();
      productTypes.add(this.toProductType());
      bundle.setProductTypes(productTypes);
      bundle.setAvailableForRetailSale(true);
      bundle.setRetailPrice(this.retailPrice);
      bundle.setRetailPriceCurrencyCode(this.retailPriceCurrencyCode);
      return bundle;
   }

   public CommercialProduct toCommercialProduct() {
      AccountingPeriod accountingPeriod = this.toAccountingPeriod();
      ProductComponent productComponent = new ProductComponent();
      productComponent.setRegion(this.toRegion());
      productComponent.setProductType(this.toProductType());
      productComponent.setAccountingPeriod(accountingPeriod);
      CommercialProduct commercialProduct = new CommercialProduct();
      commercialProduct.setProductComponent(productComponent);
      commercialProduct.setBundle(this.toBundle());
      return commercialProduct;
   }

   public AvailableFormat[] toAvailableFormats() {
      List<FileFormat> fileFormats = this.toFileFormatList();
      AvailableFormat[] availableFormats = new AvailableFormat[fileFormats.size()];
      for(int i=0; i<fileFormats.size(); i++){
         FileFormat fileFormat = fileFormats.get(i);
         AvailableFormat availableFormat = new AvailableFormat();
         availableFormat.setCommercialProduct(this.toCommercialProduct());
         availableFormat.setFileFormat(fileFormat);
         availableFormat.setAvailabilityStatus(this.toAvailabilityStatus());
         availableFormats[i] = availableFormat;
      }
      return availableFormats;
   }
}