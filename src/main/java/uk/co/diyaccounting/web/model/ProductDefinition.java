package uk.co.diyaccounting.web.model;

import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Product definitions which are used to seed the catalogue
 * 
 * @author antony
 */
public class ProductDefinition {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProductDefinition.class);

	/**
	 * The name of this item's region
	 */
	//@NotNull
	private String regionName;

	/**
	 * The name of this item's primary function
	 */
	//@NotNull
	private String primaryFunctionName;

	/**
	 * The name of this item in the product catalogue
	 */
	//@NotNull
	private String catalogueName;

	/**
	 * The name of a content item in the CMS associated with the product
	 */
	//@NotNull
	private String contentItemName;

	/**
	 * The gross price (inclusive of tax) for this product if bought right now
	 */
	//@NotNull
	private Float grossPrice;

	/**
	 * The currency in which the price is quoted
	 */
	//@NotNull
	private String currency;

	/**
	 * Should this product be available for retail sale anywhere
	 */
	//@NotNull
	private Boolean availableForRetailSale;

	/**
	 * The last day of the first accounting period this product is available for
	 */
	//@NotNull
	private DateTime firstAccountingPeriodEnd;

	/**
	 * The last day of the last accounting period this product is available for
	 */
	//@NotNull
	private DateTime lastAccountingPeriodEnd;

	/**
	 * The amount of time each instance of this product covers
	 * (typically 1 year)
	 */
	//@NotNull
	private Period accountingPeriodLength;

	/**
	 * The time interval between the year ends of each accounting period
	 * (1 month for company accounts and 1 year for everything else)
	 */
	//@NotNull
	private Period yearEndFrequency;

	/**
	 * Bundles associated with this product
	 */
	private Collection<ProductDefinition> bundles = new ArrayList<ProductDefinition>();
	
	/**
	 * Validation instance
	 */
	//private Validator validator;

	/**
	 * A validator is created in the constructor
	 */
	public ProductDefinition(){
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		//this.validator = factory.getValidator();
	}

	/**
	 * Get the region name
	 *
	 * @return the region name
	 */
	public String getRegionName() {
		return this.regionName;
	}

	/**
	 * Set the region name
	 *
	 * @param regionName the catalogue name
	 */
	public void setRegionName(final String regionName) {
		this.regionName = regionName;
	}

	/**
	 * Get the primary function name
	 *
	 * @return the primary function name
	 */
	public String getPrimaryFunctionName() {
		return primaryFunctionName;
	}

	/**
	 * Set the primary function name
	 *
	 * @param primaryFunctionName the primary function name
	 */
	public void setPrimaryFunctionName(final String primaryFunctionName) {
		this.primaryFunctionName = primaryFunctionName;
	}


	/**
	 * Get the catalogue name
	 *
	 * @return the catalogue name
	 */
	public String getCatalogueName() {
		return catalogueName;
	}

	/**
	 * Set the catalogue name
	 *
	 * @param catalogueName the catalogue name
	 */
	public void setCatalogueName(final String catalogueName) {
		this.catalogueName = catalogueName;
	}

	/**
	 * Get the content item name
	 *
	 * @return the content item name
	 */
	public String getContentItemName() {
		return contentItemName;
	}

	/**
	 * Set the content item name
	 *
	 * @param contentItemName the content item name
	 */
	public void setContentItemName(final String contentItemName) {
		this.contentItemName = contentItemName;
	}

	/**
	 * Get the gross price
	 *
	 * @return the gross price
	 */
	public Float getGrossPrice() {
		return grossPrice;
	}

	/**
	 * Set the gross price
	 *
	 * @param grossPrice the gross price
	 */
	public void setGrossPrice(final Float grossPrice) {
		this.grossPrice = grossPrice;
	}

	/**
	 * Get the currency
	 *
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Set the currency
	 *
	 * @param currency the currency
	 */
	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	/**
	 * Is this product currently available for retail sale?
	 *
	 * @return true if the product is available for retail sale, false otherwise
	 */
	public boolean isAvailableForRetailSale() {
		return BooleanUtils.isTrue(this.getAvailableForRetailSale());
	}

	/**
	 * Is this product currently available for retail sale?
	 *
	 * @return true if the product is available for retail sale, false otherwise
	 */
	public Boolean getAvailableForRetailSale() {
		return this.availableForRetailSale;
	}

	/**
	 * Set whether or not the product should be available for retail sale
	 *
	 * @param availableForRetailSale whether or not the product should be available for retail sale
	 */
	public void setAvailableForRetailSale(final Boolean availableForRetailSale) {
		this.availableForRetailSale = availableForRetailSale;
	}

	/**
	 * Get the first accounting period end
	 *
	 * @return the first accounting period end
	 */
	public DateTime getFirstAccountingPeriodEnd() {
		return firstAccountingPeriodEnd;
	}

	/**
	 * Set the date of the first accounting period
	 *
	 * @param firstAccountingPeriodEnd the date of the first accounting period
	 */
	public void setFirstAccountingPeriodEnd(final DateTime firstAccountingPeriodEnd) {
		this.firstAccountingPeriodEnd = firstAccountingPeriodEnd;
	}

	/**
	 * Get the last accounting period end
	 *
	 * @return the last accounting period end
	 */
	public DateTime getLastAccountingPeriodEnd() {
		return lastAccountingPeriodEnd;
	}

	/**
	 * Set the date of the last accounting period
	 *
	 * @param lastAccountingPeriodEnd the date of the last accounting period
	 */
	public void setLastAccountingPeriodEnd(final DateTime lastAccountingPeriodEnd) {
		this.lastAccountingPeriodEnd = lastAccountingPeriodEnd;
	}

	/**
	 * Get the accounting period length
	 *
	 * @return the accounting period length
	 */
	public Period getAccountingPeriodLength() {
		return accountingPeriodLength;
	}

	/**
	 * Set the accounting period length
	 *
	 * @param accountingPeriodLength the accounting period length
	 */
	public void setAccountingPeriodLength(final Period accountingPeriodLength) {
		this.accountingPeriodLength = accountingPeriodLength;
	}

	/**
	 * Get the year end frequency
	 *
	 * @return the year end frequency
	 */
	public Period getYearEndFrequency() {
		return yearEndFrequency;
	}

	/**
	 * set the year end frequency
	 *
	 * @param yearEndFrequency the year end frequency
	 */
	public void setYearEndFrequency(final Period yearEndFrequency) {
		this.yearEndFrequency = yearEndFrequency;
	}

	/**
	 * Does the object pass the validation checks specified in the annotations
	 *
	 * @return true, if all attributes are valid, false otherwise
	 */
	public boolean isValid(){
		//Set<ConstraintViolation<ProductDefinition>> constraintViolations = this.validator.validate(this);
		//for(ConstraintViolation<ProductDefinition> constraintViolation : constraintViolations){
		//	logger.warn("{}: {}", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
		//}
		//return constraintViolations.isEmpty();
		return true;
	}

	/**
	 * Get the bundles
	 *
	 * @return the bundles
	 */
	public Collection<ProductDefinition> getBundles() {
		return bundles;
	}

	/**
	 * set the bundles
	 *
	 * @param bundles the bundles
	 */
	public void setBundles(final Collection<ProductDefinition> bundles) {
		this.bundles = bundles;
	}
}
