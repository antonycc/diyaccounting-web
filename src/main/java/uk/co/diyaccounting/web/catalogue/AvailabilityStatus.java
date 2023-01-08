package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * The availability status as a retail product which may also reflect the software lifecycle.
 * 
 * @author Antony
 */
public class AvailabilityStatus extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   /**
    * Matcher for released status
    */
   private static Pattern releaseStatusPattern = Pattern.compile("released", Pattern.CASE_INSENSITIVE);

   /**
    * The primary key of the entity
    */
   private UUID id;

   /**
    * The name for a status as used in the catalogue. e.g. released, development.
    */
   private String name;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public AvailabilityStatus(){
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
    * The name for a status as used in the catalogue. e.g. released, development.
    * 
    * @return the name
    */
   public String getName() {
      return this.name;
   }

   /**
    * The name for a status as used in the catalogue. e.g. released, development.
    * 
    * @param name
    *           the name to set
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * Is the status released
    * 
    * @return true is the status is released
    */
   public boolean isReleased() {
      return AvailabilityStatus.releaseStatusPattern.matcher(this.getName()).matches();
   }
}