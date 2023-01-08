package uk.co.diyaccounting.web.service;

import uk.co.diyaccounting.web.content.Page;

/**
 * Content operations for page level content
 * 
 * @author Antony
 */
public interface PageService {

   Page[] getAllPages();

   Page getPage(String pageName);

   public void setPageContentBasePath(final String pageContentBasePath);

   public String getPageContentBasePath();
}
