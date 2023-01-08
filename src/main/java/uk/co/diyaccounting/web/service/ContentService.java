package uk.co.diyaccounting.web.service;

import uk.co.diyaccounting.web.content.Article;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Page;
import uk.co.diyaccounting.web.content.Product;

/**
 * Content item retrieval
 * 
 * @author Antony
 */
public interface ContentService {

   Page getPage(String path);

   String[] getPageNames(final String path);

   Product getProduct(String path);

   String[] getProductNames(final String path);

   Feature getFeature(String path);

   String[] getFeatureNames(final String path);

   Article getArticle(String path);

   String[] getArticleNames(final String path);
}
