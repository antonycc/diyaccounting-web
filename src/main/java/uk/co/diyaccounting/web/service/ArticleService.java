package uk.co.diyaccounting.web.service;

import uk.co.diyaccounting.web.content.Article;

/**
 * Content operations for article content
 * 
 * @author Antony
 */
public interface ArticleService {

   /**
    * Return all the articles with their content objects populated
    * 
    * @return all the article content
    */
   Article[] getAllArticles();

   /**
    * Return the named article
    * 
    * @param name
    *           the name of the article to get
    * @return the article content
    */
   Article getArticle(String name);

   /**
    * Set the article content base path
    *
    * @param articleContentBasePath
    *           the articleContentBasePath to set
    */
   void setArticleContentBasePath(final String articleContentBasePath);

   /**
    * The article content base path
    *
    * @return articleContentBasePath
    */
   String getArticleContentBasePath();
}
