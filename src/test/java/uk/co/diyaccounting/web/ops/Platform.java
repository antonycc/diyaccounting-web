package uk.co.diyaccounting.web.ops;

import java.io.Serializable;
import java.util.UUID;

/**
 * A platform that a product could be used on
 * 
 * @author Antony
 */
public class Platform implements Serializable {

	private static final long serialVersionUID = 1L;

   /**
    * The primary key of the entity
    */
   private UUID id;

   /**
    * The name of the platform. e.g. Microsoft Excel 2010
    */
   private String name;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public Platform(){
   }

   /**
    * A string version including the unique attribute(s) which make up the primary key of this entity
    * 
    * @return A string version of this entity
    */
   @Override
   public String toString() {
      return this.getName();
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
    * The name of the platform. e.g. Microsoft Excel 2010
    * 
    * @return the name
    */
   public String getName() {
      return this.name;
   }

   /**
    * The name of the platform. e.g. Microsoft Excel 2010
    * 
    * @param name
    *           the name to set
    */
   public void setName(final String name) {
      this.name = name;
   }

}