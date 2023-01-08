package uk.co.diyaccounting.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import uk.co.diyaccounting.web.DiyAccountingSpringBootConfiguration;
import uk.co.diyaccounting.web.catalogue.ProductType;
import uk.co.diyaccounting.web.content.Article;
import uk.co.diyaccounting.web.model.Site;
import uk.co.polycode.mdcms.cms.dto.AbstractItem;
import uk.co.polycode.mdcms.cms.dto.NameBody;
import uk.co.polycode.mdcms.util.reflect.ReflectionHelper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Handles requests for the application's simple request to view mappings
 */
@Service("controllerUtils")
public class ControllerUtils {

   /**
    * The number of minutes that a page should be cached
    */
   public static final int PAGE_EXPIRY = 10;

   /**
    * The pattern for formatting dates in headers
    */
   public static final String rfc2822DatePattern = "EEE, dd MMM yyyy HH:mm:ss Z";

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(ControllerUtils.class);

   /**
    * The size (in digits) for the digest
    */
   private static final int digestSize = 16;

   /**
    * The application config
    */
   @Autowired
   //@Qualifier("appConfigController")
   private DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration;

   /**
    * The site properties
    */
   @Autowired
   private Site site;

   /**
    * The reflection helper delegate
    */
   private transient ReflectionHelper reflect = new ReflectionHelper();

   private final Date startUp = new Date();

   /**
    * Return the a ModelAndView with utilities
    *
    * @return the model and view to render
    */
   public HashMap<String, Object> buildResponseModelMapWithSiteDetails() {
      HashMap<String, Object> model = new HashMap<>();

      // Site
      if(this.site != null) {
         HashMap<String, Object> siteModel = new HashMap<>();
         siteModel.put("language", this.site.getLanguage());
         siteModel.put("url", this.site.getUrl());
         siteModel.put("name", this.site.getName());
         siteModel.put("fb", this.site.getFb());
         siteModel.put("logo", this.site.getLogo());
         siteModel.put("organisation", this.site.getOrganisation());
         siteModel.put("paypalHostedButtonType", this.site.getPaypalHostedButtonType());
         siteModel.put("paypalHostedButtonId", this.site.getPaypalHostedButtonId());
         siteModel.put("paypalHostedButtonEnvironment", this.site.getPaypalHostedButtonEnvironment());
         model.put("site", siteModel);
      }

      return model;
   }

   /**
    * Return the a model with simple attributes
    *
    * @return the model
    */
   public HashMap<String, Object> nameBodyAsMap(NameBody nameBody) {
      HashMap<String, Object> model = new HashMap<>();
      model.put("name", nameBody.getName());
      model.put("nameWithoutPostfix", nameBody.getNameWithoutPostfix());
      model.put("body", nameBody.getBody());
      return model;
   }

   /**
    * Return the a model with simple attributes
    *
    * @return the model
    */
   public HashMap<String, Object> articleAsMap(Article article) {
      HashMap<String, Object> model = new HashMap<>();
      model.put("name", article.getName());
      model.put("title", article.getTitle());
      model.put("description", article.getDescription());
      model.put("featured", article.getFeatured());
      model.put("whatsnew", article.getWhatsnew());
      return model;
   }

   /**
    * Generate a bean method name from the field
    * 
    * @param prefix
    *           the bean method prefix
    * 
    * @return the bean setter or getter name
    */
   private String getBeanMethodName(final String prefix, final String fieldName) {
      StringBuilder buf = new StringBuilder();
      buf.append(prefix);
      buf.append(Character.toUpperCase(fieldName.charAt(0)));
      buf.append(fieldName.substring(1));
      return buf.toString();
   }

   /**
    * Generate an URL that can be used an internal forward to the relevant controller
    * 
    * @param productType
    *           the productType to generate as a friendly Url
    * @param path
    *           the path of a URL that may need translating
    * 
    * @return the internal representation of the path
    */
   public String generateFriendlyUrlForConfirmedPage(final ProductType productType, final String path) {
      String productCatalogueName = productType.getCatalogueName();
      String productAsFriendlyUrl = productCatalogueName.replaceAll(" ", "-");
      return productAsFriendlyUrl + "-" + path;
   }

   /**
    * Set cache headers in the response for the given model and view
    *
    * @param response
    *           the HTTP response for adding headers
    * @param model
    *           the model map that is going on the page to be cached
    * @param contentType
    *           the media type of the content to be dispatched
    */
   public void setHeaders(final HttpServletResponse response,
                          final HashMap<String, Object> model, final String contentType) {

      // Don't set the headers if this is not being called for a response
      if (response == null) {
         return;
      }

      // Etag - generated from serialisable models passed to the view
      Object[] values = model.values().toArray();
      ArrayList<Object> contentItems = new ArrayList<Object>();
      for (Object value : values) {
         // Include only objects that are serializable
         if (value instanceof AbstractItem) {
            contentItems.add(value);
         }
         if (value instanceof AbstractItem[]) {
            contentItems.add(value);
         }
      }
      byte[] bytes = SerializationUtils.serialize(contentItems.toArray());
      String token = this.getDigest("MD5", bytes);
      response.setHeader("Etag", token);

      // Last-Modified
      response.setHeader("Content-Type", contentType);

      // Last-Modified
      SimpleDateFormat format = new SimpleDateFormat(ControllerUtils.rfc2822DatePattern, Locale.ENGLISH);
      String lastModified = format.format(this.startUp);
      response.setHeader("Last-Modified", lastModified);

      // Expires
      Date now = new Date();
      Date expiresDate = DateUtils.addMinutes(now, ControllerUtils.PAGE_EXPIRY);
      String expires = format.format(expiresDate);
      response.setHeader("Expires", expires);
   }


   /**
    * Set cache headers in the response for the given model and view
    * 
    * @param response
    *           the HTTP response for adding headers
    * @param modelAndView
    *           the model and view that are going on the page to be cached
    * @param contentType
    *           the media type of the content to be dispatched
    */
   public void setHeaders(final HttpServletResponse response,
            final ModelAndView modelAndView, final String contentType) {

      // Don't set the headers if this is not being called for a response
      if (response == null) {
         return;
      }

      // Etag - generated from serialisable models passed to the view
      ModelMap modelMap = modelAndView.getModelMap();
      Object[] values = modelMap.values().toArray();
      ArrayList<Object> contentItems = new ArrayList<Object>();
      for (Object value : values) {
         // Include only objects that are serializable
         if (value instanceof AbstractItem) {
            contentItems.add(value);
         }
         if (value instanceof AbstractItem[]) {
            contentItems.add(value);
         }
      }
      byte[] bytes = SerializationUtils.serialize(contentItems.toArray());
      String token = this.getDigest("MD5", bytes);
      response.setHeader("Etag", token);

      // Last-Modified
      response.setHeader("Content-Type", contentType);

      // Last-Modified
      SimpleDateFormat format = new SimpleDateFormat(ControllerUtils.rfc2822DatePattern, Locale.ENGLISH);
      String lastModified = format.format(this.startUp);
      response.setHeader("Last-Modified", lastModified);

      // Expires
      Date now = new Date();
      Date expiresDate = DateUtils.addMinutes(now, ControllerUtils.PAGE_EXPIRY);
      String expires = format.format(expiresDate);
      response.setHeader("Expires", expires);
   }

   /**
    * Generate an digest using the supplied bytes and the specified algorithm
    *
    * @param algorithm
    *           the algorithm to use e.g. MD5
    * @param bytes
    *           the bytes to digest
    *
    * @return a digest of the bytes
    */
   public String getDigest(final String algorithm, final byte[] bytes) {
      MessageDigest md;
      try {
         md = MessageDigest.getInstance(algorithm);
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("Cryptographic algorithm " + algorithm + " is not available.", e);
      }
      byte[] messageDigest = md.digest(bytes);
      BigInteger number = new BigInteger(1, messageDigest);
      // prepend a zero to get a "proper" MD5 hash value
      StringBuilder sb = new StringBuilder('0');
      sb.append(number.toString(ControllerUtils.digestSize));
      return sb.toString();
   }

   public void setAppConfig(final DiyAccountingSpringBootConfiguration diyAccountingSpringBootConfiguration) {
      this.diyAccountingSpringBootConfiguration = diyAccountingSpringBootConfiguration;
   }

   public void setReflect(final ReflectionHelper reflect) {
      this.reflect = reflect;
   }
}
