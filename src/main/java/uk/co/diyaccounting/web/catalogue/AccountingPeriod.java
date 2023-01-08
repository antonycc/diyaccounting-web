package uk.co.diyaccounting.web.catalogue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * An accounting period that a set of accounts will span.
 * 
 * @author Antony
 */
public class AccountingPeriod extends ComparableUsingString implements Serializable {

   @JsonIgnore
   private static final long serialVersionUID = 1L;

   /**
    * The primary key of the entity
    */
   private UUID id;

   /**
    * The name of the period as it appears in the product catalogue e.g. 2008-04-05 (Apr08).
    */
   private String name;

   /**
    * The first day of the accounting period. e.g. 6th April 2007
    */
   private Date startDate;

   /**
    * The last day of the accounting period. e.g. 5th April 2008
    */
   private Date endDate;

   /**
    * No arg constructor see:
    * https://stackoverflow.com/questions/2935826/why-does-hibernate-require-no-argument-constructor/2971717#2971717
    */
   public AccountingPeriod(){
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
    * The name of the period as it appears in the product catalogue e.g. 2008-04-05 (Apr08).
    * 
    * @return the name
    */
   public String getName() {
      return this.name;
   }

   /**
    * The name of the period as it appears in the product catalogue e.g. 2008-04-05 (Apr08).
    * 
    * @param name
    *           the name to set
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * The first day of the accounting period. e.g. 6th April 2007
    * 
    * @return the startDate
    */
   public Date getStartDate() {
      return this.startDate;
   }

   /**
    * The first day of the accounting period. e.g. 6th April 2007
    * 
    * @param startDate
    *           the startDate to set
    */
   public void setStartDate(final Date startDate) {
      this.startDate = startDate;
   }

   /**
    * The last day of the accounting period. e.g. 5th April 2008
    * 
    * @return the endDate
    */
   public Date getEndDate() {
      return this.endDate;
   }

   /**
    * The last day of the accounting period. e.g. 5th April 2008
    * 
    * @param endDate
    *           the endDate to set
    */
   public void setEndDate(final Date endDate) {
      this.endDate = endDate;
   }

}