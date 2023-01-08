package uk.co.diyaccounting.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.diyaccounting.web.content.Page;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Accesses page level content
 */
@Service("pageContent")
public class PageServiceImpl implements PageService {

   @Autowired
   @Qualifier("content")
   private ContentService content;

   private Page page = new Page();

   @Value("${content.pageContentBasePath}")
   private String pageContentBasePath;

   @Override
   public Page[] getAllPages() {
      String[] pageNames = this.content.getPageNames(this.getPageContentBasePath());

      // Build an array of populated pages
      ArrayList<Page> pageVector = new ArrayList<Page>();
      for (String pageName : pageNames) {

         // Populate the pages with data from the CMS
         Page populatedPage;
         String path = this.getPageContentBasePath() + pageName + this.page.getExtension();
         populatedPage = this.content.getPage(path);

         pageVector.add(populatedPage);
      }
      Page[] populatedPages = pageVector.toArray(new Page[pageVector.size()]);

      // Sort and return
      Arrays.sort(populatedPages);
      return populatedPages;
   }

   @Override
   public Page getPage(final String pageName) {
      String path = this.getPageContentBasePath() + pageName + this.page.getExtension();
      return this.content.getPage(path);
   }

   public void setContent(final ContentService content) {
      this.content = content;
   }

   @Override
   public void setPageContentBasePath(final String pageContentBasePath) {
      this.pageContentBasePath = pageContentBasePath;
   }

   @Override
   public String getPageContentBasePath() {
      return this.pageContentBasePath;
   }

}
