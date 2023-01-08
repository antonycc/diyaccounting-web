package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;
import java.util.UUID;

/**
 * An region that a package is applicable to, typically a geographical region with a single set of tax rules
 * 
 * @author Antony
 */
public class Region extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   private UUID id;

   /**
    * The name of the region a product would be suitable for. e.g. GB
    */
   private String name;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public Region(){
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.catalogue.dto.Unique#setUniqueParameters(javax.persistence.Query)
    */
//   @Override
//   public void setUniqueParameters(final Query query) {
//      query.setParameter("name", this.getName());
//   }
//
//   @Override
//   public void copyAttributesForUpdate(final ComparableUsingString o) {
//      Region entity = (Region)o;
//      this.setName(entity.getName());
//   }

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
      buf.append(this.getName());
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
    * The name of the region a product would be suitable for. e.g. GB
    * 
    * @return the name
    */
   public String getName() {
      return this.name;
   }

   /**
    * The name of the region a product would be suitable for. e.g. GB
    * 
    * @param name
    *           the name to set
    */
   public void setName(final String name) {
      this.name = name;
   }

}