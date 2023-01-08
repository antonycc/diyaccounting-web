package uk.co.diyaccounting.web.ops;

import uk.co.polycode.mdcms.cms.dto.AbstractItem;

/**
 * A simple bean to test bean operations
 * 
 * @author Antony
 */
public class TwoFieldBean extends AbstractItem {

	private static final long serialVersionUID = 1L;

   /**
    * The items stringField1
    */
   private String stringField1;

   /**
    * The items stringField2
    */
   private String stringField2;

   @Override
   public String getExtension(){
      return null;
   }

   @Override
   public String getFilter(){
      return null;
   }

   /**
    * Get the stringField1
    * 
    * @return the stringField1
    */
   public String getStringField1() {
      return this.stringField1;
   }

   /**
    * Set the stringField1
    * 
    * @param stringField1
    *           the new stringField1
    */
   public void setStringField1(final String stringField1) {
      this.stringField1 = stringField1;
   }

   /**
    * Get the stringField1
    * 
    * @return the stringField1
    */
   public String getStringField2() {
      return this.stringField2;
   }

   /**
    * Set the stringField2
    * 
    * @param stringField2
    *           the new stringField2
    */
   public void setStringField2(final String stringField2) {
      this.stringField2 = stringField2;
   }
}
