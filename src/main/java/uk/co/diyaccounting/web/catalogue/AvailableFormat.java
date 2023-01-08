package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;
import java.util.UUID;

/**
 * A commercial product that is available in a specific format
 * 
 * @author Antony
 */
public class AvailableFormat extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   /**
    * The generated serialisation ID for the class
    */
   @JsonIgnore
   public static final String FIND_FILE_FORMATS_QUERY = "findFileFormatsForCommercialProduct";

   private UUID id;


   /**
    * The product component that will be available in the specified format
    */
   private CommercialProduct commercialProduct;

   /**
    * The file format this product uses.
    */
   private FileFormat fileFormat;

   /**
    * This commercial products availability for sale.
    */
   private AvailabilityStatus availabilityStatus;

	/**
	 * The filename part of the download URL. e.g. 0708basicxm7wv
	 */
	private String downloadUrlFilename;

	/**
	 * No arg constructor see:
	 * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
	 */
	public AvailableFormat(){
	}

   /**
    * A string version including the unique attribute(s) which make up the primary key of this entity
    * 
    * @return A string version of this entity
    */
   @Override
   public String toString() {
      StringBuilder buf = new StringBuilder();
      buf.append("{");
      buf.append(this.getClass().getSimpleName());
      buf.append(":");
      buf.append(this.getCommercialProduct() != null ? this.getCommercialProduct().toString() : "null");
      buf.append(",");
      buf.append(this.getFileFormat() != null ? this.getFileFormat().toString() : "null");
      buf.append("}");
      return buf.toString();
   }

   /**
    * The primary key of the entity
    * 
    * @return the id
    */
   public UUID getId() {
      return this.id;
   }

   /**
    * The primary key of the entity
    * 
    * @param id
    *           the id to set
    */
   public void setId(final UUID id) {
      this.id = id;
   }

   /**
    * @return the commercialProduct
    */
   public CommercialProduct getCommercialProduct() {
      return this.commercialProduct;
   }

   /**
    * @param commercialProduct
    *           the commercialProduct to set
    */
   public void setCommercialProduct(final CommercialProduct commercialProduct) {
      this.commercialProduct = commercialProduct;
   }

   /**
    * The file format the product will be available in
    * 
    * @return the fileFormat
    */
   public FileFormat getFileFormat() {
      return this.fileFormat;
   }

   /**
    * The file format the product will be available in
    * 
    * @param fileFormat
    *           the fileFormat to set
    */
   public void setFileFormat(final FileFormat fileFormat) {
      this.fileFormat = fileFormat;
   }

   /**
    * This commercial products availability for sale.
    * 
    * @return the availabilityStatus
    */
   public AvailabilityStatus getAvailabilityStatus() {
      return this.availabilityStatus;
   }

   /**
    * This commercial products availability for sale.
    * 
    * @param availabilityStatus
    *           the availabilityStatus to set
    */
   public void setAvailabilityStatus(final AvailabilityStatus availabilityStatus) {
      this.availabilityStatus = availabilityStatus;
   }


	/**
	 * The filename part of the download URL. e.g. 0708basicxm7wv
	 *
	 * @return the downloadUrlFilename
	 */
	public String getDownloadUrlFilename() {
		return this.downloadUrlFilename;
	}

	/**
	 * The filename part of the download URL. e.g. 0708basicxm7wv
	 *
	 * @param downloadUrlFilename
	 *           the downloadUrlFilename to set
	 */
	public void setDownloadUrlFilename(final String downloadUrlFilename) {
		this.downloadUrlFilename = downloadUrlFilename;
	}
}