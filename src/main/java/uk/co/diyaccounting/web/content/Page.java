package uk.co.diyaccounting.web.content;


import uk.co.polycode.mdcms.cms.dto.Metadata;

public class Page extends Metadata {

	private static final long serialVersionUID = 1L;

   @Override
   public String getExtension(){
      return ".md";
   }

   @Override
   public String getFilter(){
      return "Page" + this.getExtension();
   }

   @Override
   public void postPopulationConfig() {
      super.postPopulationConfig();
   }
}
