package uk.co.diyaccounting.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Product;
import uk.co.diyaccounting.web.service.ArticleService;
import uk.co.diyaccounting.web.service.PageService;
import uk.co.diyaccounting.web.service.ProductService;

import java.util.Set;
import java.util.TreeSet;

/**
 * Handles requests for the application's simple request to view mappings
 */
@Controller("siteMapController")
public class SiteMapController {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(SiteMapController.class);

   /**
    * Page Content
    */
   @Autowired
   @Qualifier("pageContent")
   private PageService pageContent;

   /**
    * Product Content
    */
   @Autowired
   @Qualifier("productContent")
   private ProductService productContent;

   /**
    * Article Content
    */
   @Autowired
   @Qualifier("articleContent")
   private ArticleService articleContent;

   /**
    * Common utilities extracted into a shared bean
    */
   @Autowired
   @Qualifier("controllerUtils")
   private ControllerUtils controllerUtils;

   /**
    * Return the DIY Accounting sitemap from a Freemarker template
    * 
    * @return the DIY Accounting sitemap from a Freemarker template
    */
   @RequestMapping(value = "/sitemap.xml")
   public ModelAndView sitemap(final HttpServletResponse response) {
      SiteMapController.logger.info("/sitemap.xml");

      // Populate model and view
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.setViewName("sitemap");

      // Pages
      modelAndView.addObject("home", this.pageContent.getPage("HomePage"));
      modelAndView.addObject("pages", this.pageContent.getAllPages());

      // Products
      Product[] products = this.productContent.getAllProducts();
      modelAndView.addObject("products", products);

      // Features
      Set<Feature> featureSet = new TreeSet<Feature>();
      for (Product product : products) {
         String[] featureNames = product.getFeatureNames();
         Feature[] features = this.productContent.getFeatures(featureNames);
         if (features != null) {
            for (Feature feature : features) {
               if(feature != null) {
                  featureSet.add(feature);
               }
            }
         }
      }
      Feature[] features = new Feature[featureSet.size()];
      features = featureSet.toArray(features);
      modelAndView.addObject("features", features);

      // Articles
      modelAndView.addObject("articles", this.articleContent.getAllArticles());

      // Headers
      this.controllerUtils.setHeaders(response, modelAndView, MediaType.TEXT_XML_VALUE);

      return modelAndView;
   }

   /**
    * Set the page service
    * 
    * @param pageContent
    *           the page service
    */
   public void setPageContent(final PageService pageContent) {
      this.pageContent = pageContent;
   }

   /**
    * Set the product service
    * 
    * @param productContent
    *           the product service
    */
   public void setProductContent(final ProductService productContent) {
      this.productContent = productContent;
   }

   /**
    * Set the article service
    * 
    * @param articleContent
    *           the article service
    */
   public void setArticleContent(final ArticleService articleContent) {
      this.articleContent = articleContent;
   }

   /**
    * Set the controller utils
    * 
    * @param controllerUtils
    *           the controller utils
    */
   public void setControllerUtils(final ControllerUtils controllerUtils) {
      this.controllerUtils = controllerUtils;
   }

}
