package uk.co.diyaccounting.web.content;

import uk.co.polycode.mdcms.cms.dto.Metadata;
import uk.co.polycode.mdcms.cms.type.MdContent;

public final class Article extends Metadata {

   private static final long serialVersionUID = 1L;

   @MdContent(path = "/whatsnew")
   private boolean whatsnew;

   @Override
   public String getExtension(){
      return ".md";
   }

   @Override
   public String getFilter(){
      return "Article" + this.getExtension();
   }

   public boolean getWhatsnew() {
      return this.whatsnew;
   }

   public void setWhatsnew(final boolean whatsnew) {
      this.whatsnew = whatsnew;
   }
}
