package uk.co.diyaccounting.web.service;

import uk.co.diyaccounting.web.catalogue.CommercialProduct;

import java.text.ParseException;
import java.util.Properties;

/**
 * Top level catalogue services
 * 
 * @author Antony
 */
public interface IngestService {

   /**
    * Remove all data from a catalogue
    */
   void clearCatalogue();

   /**
    * Injest new data from a catalogue text file
    * 
    * @param catalogueBundlePricesSource
    *           the source data for bundles and their prices
    * @param catalogueNames
    *           the source data for mappings between the names used in the catalogue and those used in the CMS
    * @param catalogueSource
    *           the source data for a catalogue
    * @throws ParseException
    */
   void ingestCatalogue(String catalogueBundlePricesSource, String catalogueNames, String catalogueSource) throws ParseException;

   /**
    * Ingest the file format from a string creating a new one if necessary. Example of a line <br/>
    * GB Accounts Basic Sole Trader 2008-04-05 (Apr08),Excel 2003,released,0708basicxm7wv.htm
    * 
    * @param catalogueSource
    *           the source data for a catalogue
    * @param line
    *           a single line of data from the catalogue
    * 
    * @return the commercial product represented by the product string
    * @throws ParseException
    */
   CommercialProduct ingestCatalogueLine(Properties catalogueNameMappings,
                                         String catalogueSource, String line) throws ParseException, CatalogueServiceException;

   /**
    * Export all data from the catalogue into a text file
    */
   String exportCatalogue();
}
