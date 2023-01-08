package uk.co.diyaccounting.web.ops;

import uk.co.diyaccounting.web.catalogue.FileFormat;

import java.io.Serializable;
import java.util.UUID;

/**
 * A platform that is able to consume a given file format
 * 
 * @author Antony
 */
public class SupportedPlatform implements Serializable {

	private static final long serialVersionUID = 1L;

   /**
    * The primary key of the entity
    */
   private UUID id;

   /**
    * The format that will be supported by the specified platform
    */
   private FileFormat fileFormat;

   /**
    * The platform that will support the specified file format
    */
   private Platform platform;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public SupportedPlatform(){
   }

   /**
    * A string version including the unique attribute(s) which make up the primary key of this entity
    * 
    * @return A string version of this entity
    */
   @Override
   public String toString() {
      return this.getFileFormat().toString() + this.getPlatform().toString();
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
    * The format that will be supported by the specified platform
    * 
    * @return the fileFormat
    */
   public FileFormat getFileFormat() {
      return this.fileFormat;
   }

   /**
    * The format that will be supported by the specified platform
    * 
    * @param fileFormat
    *           the fileFormat to set
    */
   public void setFileFormat(final FileFormat fileFormat) {
      this.fileFormat = fileFormat;
   }

   /**
    * The platform that will support the specified file format
    * 
    * @return the platform
    */
   public Platform getPlatform() {
      return this.platform;
   }

   /**
    * The platform that will support the specified file format
    * 
    * @param platform
    *           the platform to set
    */
   public void setPlatform(final Platform platform) {
      this.platform = platform;
   }

}