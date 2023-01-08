package uk.co.diyaccounting.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.co.diyaccounting.web.content.Article;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Page;
import uk.co.diyaccounting.web.content.Product;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.util.Date;

@Service("content")
public class ContentServiceImpl implements ContentService {

   private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

   @Autowired
   @Qualifier("fileLikePaths")
   private FileLikePathService fileLikePathService;

   @Value("${content.imageContentPrefix}")
   private String imageContentPrefix;

   /**
    * A static setting the last modified time as the app start time
    */
   private final Date startUp = new Date();

   @Override
   public Page getPage(final String path) {
      Page page = new Page();
      page.setPath(path);
      page.setImagePrefix(this.imageContentPrefix);
      page.populateContent(this.fileLikePathService);
      return page;
   }

   @Override
   public String[] getPageNames(final String path) {
      Page page = new Page();
      page.setPath(path);
      String[] pageNames = page.contentList(this.fileLikePathService);
      return pageNames;
   }

   @Override
   public Product getProduct(final String path) {
      Product product = new Product();
      product.setPath(path);
      product.setImagePrefix(this.imageContentPrefix);
      product.populateContent(this.fileLikePathService);
      return product;
   }

   @Override
   public String[] getProductNames(final String path) {
      Product product = new Product();
      product.setPath(path);
      String[] productNames = product.contentList(this.fileLikePathService);
      return productNames;
   }

   @Override
   public Feature getFeature(final String path) {
      Feature feature = new Feature();
      feature.setPath(path);
      feature.setImagePrefix(this.imageContentPrefix);
      feature.populateContent(this.fileLikePathService);
      return feature;
   }

   @Override
   public String[] getFeatureNames(final String path) {
      Feature feature = new Feature();
      feature.setPath(path);
      String[] featureNames = feature.contentList(this.fileLikePathService);
      return featureNames;
   }

   @Override
   public Article getArticle(final String path) {
      Article article = new Article();
      article.setPath(path);
      article.setImagePrefix(this.imageContentPrefix);
      article.populateContent(this.fileLikePathService);
      return article;
   }

   @Override
   public String[] getArticleNames(final String path) {
      Article article = new Article();
      article.setPath(path);
      String[] articleNames = article.contentList(this.fileLikePathService);
      return articleNames;
   }

   public void setFileHelper(final FileLikePathService fileLikePathService) {
      this.fileLikePathService = fileLikePathService;
   }

   public String getImageContentPrefix() {
      return imageContentPrefix;
   }

   public void setImageContentPrefix(final String imageContentPrefix) {
      this.imageContentPrefix = imageContentPrefix;
   }
}
