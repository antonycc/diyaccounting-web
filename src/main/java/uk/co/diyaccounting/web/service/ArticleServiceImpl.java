package uk.co.diyaccounting.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.diyaccounting.web.content.Article;

import java.util.Arrays;

/**
 * Accesses article level content
 */
@Service("articleContent")
public class ArticleServiceImpl implements ArticleService {

   /**
    * Content
    */
   @Autowired
   @Qualifier("content")
   private ContentService content;

   /**
    * The base path for article content
    */
   @Value("${content.articleContentBasePath}")
   private String articleContentBasePath;

   private Article article = new Article();

   /*
    * (non-Javadoc)
    * 
    * @see present.service.ArticleContentService#getAllArticles()
    */
   @Override
   public Article[] getAllArticles() {
      String[] articleList = this.content.getArticleNames(this.getArticleContentBasePath());
      Article[] populatedArticles = new Article[articleList.length];
      for (int i = 0; i < articleList.length; i++) {
         String path = this.getArticleContentBasePath() + articleList[i] + this.article.getExtension();
         populatedArticles[i] = this.content.getArticle(path);
      }
      Arrays.sort(populatedArticles);
      return populatedArticles;
   }

   /*
    * (non-Javadoc)
    * 
    * @see present.service.ArticleService#getArticle()
    */
   @Override
   public Article getArticle(final String name) {
      String path = this.getArticleContentBasePath() + name + this.article.getExtension();
	   return this.content.getArticle(path);
   }

   /**
    * Set the content service
    * 
    * @param content
    *           the content to set
    */
   public void setContent(final ContentService content) {
      this.content = content;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.diyaccounting.content.service.ArticleService#setArticleContentBasePath(java.lang.String)
    */
   @Override
   public void setArticleContentBasePath(final String articleContentBasePath) {
      this.articleContentBasePath = articleContentBasePath;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.diyaccounting.content.service.ArticleService#ArticleContentBasePath()
    */
   @Override
   public String getArticleContentBasePath() {
      return this.articleContentBasePath;
   }

}
