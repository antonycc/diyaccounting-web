package uk.co.diyaccounting.web.service;

import uk.co.diyaccounting.web.model.ProductDefinition;

import java.util.Collection;
import java.util.Properties;

/**
 * Catalogue population services
 * 
 * @author Antony
 */
public interface PopulationService {

	/**
	 * Read the definitions from an ini type file
	 *
	 * @param definitionsFile
	 *           a collection of catalogue item definitions which will expand to generate the whole catalogue
	 */
	Collection<ProductDefinition> parseProductDefinitions(String definitionsFile);

   /**
    * Ingest catalogue from product definitions
    * 
    * @param productDefinitions
    *           a collection of catalogue item definitions which will expand to generate the whole catalogue
	 */
   void ingestCatalogue(Collection<ProductDefinition> productDefinitions);

	/**
	 * Ingest standing data
	 *
	 * @param standingData name value pairs to generate standing data from
	 */
	void ingestStandingData(Properties standingData);
}
