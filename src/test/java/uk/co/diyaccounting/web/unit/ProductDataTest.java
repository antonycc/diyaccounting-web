package uk.co.diyaccounting.web.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.web.content.Product;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Test the metadata class's equality
 *
 * @author Antony
 */
public class ProductDataTest {

   private static final String localPath = Product.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private static final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private static final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";
   private FileLikePathService fileLikePathService = new FileLikePathService();

   // private static final Logger logger = LoggerFactory.getLogger(MetadataTest.class);

   /**
    * Tests hashcode
    *
    * @throws ContentException
    */
   @Test
   public void testHashCode() throws ContentException {

      String productPath = baseFileUrn + "test-content/BasicSoleTraderProduct.md";
      String productPath2 = baseFileUrn + "test-content/SelfEmployedProduct.md";

      // Expected values

      // Set up object
      Product product = new Product();
      product.setPath(productPath);
      product.populateContent(this.fileLikePathService);
      Product product2 = new Product();
      product2.setPath(productPath2);
      product2.populateContent(this.fileLikePathService);

      // Check values
      Assert.assertEquals("Hashcode should be consistent", product.hashCode(), product.hashCode());
      Assert.assertFalse("Hashcode should be consistent", product.hashCode() == product2.hashCode());
      //Assert.assertNotNull(product.getTemplate());
   }

   /**
    * Tests equals
    *
    * @throws ContentException
    */
   @Test
   public void testEquals() throws ContentException {

      // Test parameters
      String productPath = baseFileUrn + "test-content/BasicSoleTraderProduct.md";
      String productPath2 = baseFileUrn + "test-content/SelfEmployedProduct.md";

      // Expected values

      // Dump classpath
      /*
       * ClassLoader classLoader; logger.debug("Local classpath:"); classLoader = this.getClass().getClassLoader(); if(
       * classLoader instanceof URLClassLoader) { URLClassLoader urlClassLoader = ((URLClassLoader) classLoader); for
       * (URL url : urlClassLoader.getURLs()) { logger.debug("File: {}", url.getPath(), url.getFile()); try { JarFile
       * jarFile = new JarFile(url.getFile()); Enumeration<JarEntry> jarEntries = jarFile.entries();
       * while(jarEntries.hasMoreElements()){ JarEntry jarEntry = jarEntries.nextElement(); logger.debug("\t{}",
       * jarEntry.toString()); } }catch (IOException e){ logger.debug("Could not read entry as JAR file: {}",
       * e.getMessage()); } } }else{ logger.debug("The class loader is not a URLClassLoader."); }
       */

      // Set up object
      Product product = new Product();
      product.setPath(productPath);
      product.populateContent(this.fileLikePathService);
      Product product2 = new Product();
      product2.setPath(productPath2);
      product2.populateContent(this.fileLikePathService);

      // Check values
      Assert.assertTrue("Object should be equal to itsself", product.equals(product));
      Assert.assertFalse("Object should be not equal to a different object", product.equals(product2));
      Assert.assertFalse("Object should be not equal to null", product.equals(null));
      Assert.assertFalse("Object should be not equal to a String", product.equals(""));
   }
}
