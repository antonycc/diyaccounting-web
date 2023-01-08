package uk.co.diyaccounting.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.co.diyaccounting.web.catalogue.*;
import uk.co.diyaccounting.web.content.Article;
import uk.co.diyaccounting.web.content.Feature;
import uk.co.diyaccounting.web.content.Page;
import uk.co.diyaccounting.web.content.Product;
import uk.co.diyaccounting.web.model.Site;
import uk.co.diyaccounting.web.service.*;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.net.UrlHelper;

import java.util.*;

// https://spring.io/guides/gs/rest-service/
@Controller("contentApi")
@RestController
public class ContentApi {

	private static final Logger logger = LoggerFactory.getLogger(ContentApi.class);

	@Autowired
	@Qualifier("calculationUtils")
	protected CalculationUtils calculationUtils;

	@Autowired
	@Qualifier("controllerUtils")
	protected ControllerUtils controllerUtils;

	@Autowired
	@Qualifier("articleContent")
	protected ArticleService articleContent;

	@Autowired
	@Qualifier("pageContent")
	protected PageService pageContent;

	@Autowired
	@Qualifier("productContent")
	protected ProductService productContent;

	@Autowired
	@Qualifier("site")
	protected Site site;

	@Autowired
	@Qualifier("content")
	protected ContentService content;

	@Autowired
	@Qualifier("url")
	protected UrlHelper url;

	@Autowired
	@Qualifier("fileLikePaths")
	private FileLikePathService fileLikePathService;

	protected final Date startUp = new Date();

	@GetMapping(
			value = "/content/page",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> page(final HttpServletRequest request) {
		return this.buildPage(false);
	}

	@GetMapping(
			value = "/content/page-with-articles",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> pageWithArticles(final HttpServletRequest request) {
		return this.buildPage(true);
	}

	@GetMapping(
			value = "/content/articles/{articleName}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> article(
			final HttpServletRequest request,
			@PathVariable(name = "articleName", required = true) final String articleName
	) {
		String path = this.getPath(request);
		logger.info(path);
		Article article = this.articleContent.getArticle(articleName);
		HashMap<String, Object> pages = new HashMap<>();
		pages.put("article", article);
		HashMap<String, Object> model;
		model = this.getPageModel();
		model.put("pages", pages);
		return model;
	}

	@GetMapping(
			value = "/content/features/{featureName}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> feature(
			final HttpServletRequest request,
			@PathVariable(name = "featureName", required = true) final String featureName
	) {
		String path = this.getPath(request);
		logger.info(path);
		Feature feature = this.productContent.getFeature(featureName);
		HashMap<String, Object> model = this.getPageModel();
		HashMap<String, Object> pages = new HashMap<>();
		pages.put("feature", feature);
		model.put("pages", pages);
		return model;
	}

	@GetMapping(
			value = "/content/feature-for-product/{featureName}/{productName}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> featureForProduct(
			final HttpServletRequest request,
			@PathVariable(name = "featureName", required = true) final String featureName,
			@PathVariable(name = "productName", required = true) final String productName
	) {
		String path = this.getPath(request);
		logger.info(path);
		Feature feature = this.productContent.getFeature(featureName);
		Product product = this.productContent.getProduct(productName);
		HashMap<String, Object> model = this.getProductPageModel(product);
		HashMap<String, Object> pages = new HashMap<>();
		pages.put("feature", feature);
		model.put("pages", pages);
		return model;
	}

	@GetMapping(
			value = "/content/products/{productDashedName}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> product(
			final HttpServletRequest request,
			@PathVariable(name = "productDashedName", required = true) final String productDashedName
	) {
		String path = this.getPath(request);
		logger.info(path);

		String productName = productDashedName != null ? productDashedName.replace("-", " ") : null;
		HashMap<String, Object> model = this.getSelectedProductModel(productName);

		return model;
	}

	@GetMapping(
			value = "/content/product-for-period/{productDashedName}/{periodDashedName}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> productForPeriod(
			final HttpServletRequest request,
			@PathVariable(name = "productDashedName", required = true) final String productDashedName,
			@PathVariable(name = "periodDashedName", required = true) final String periodDashedName
	) {
		String path = this.getPath(request);
		logger.info(path);

		String productName = productDashedName != null ? productDashedName.replace("-", " ") : null;
		String periodName = periodDashedName != null ? periodDashedName.replace("-(", " (") : null;

		HashMap<String, Object> model = this.getSelectedProductModel(productName);

		this.populateSelectedProductAndPeriod(periodName, model);
		this.addDownloads(model, productName, periodName);

		return model;
	}

//	@GetMapping(
//			value = "/checkout/downloads/product/{productName}/period/{periodName}",
//			produces = MediaType.APPLICATION_JSON_VALUE
//	)
//	public HashMap<String, Object> download(
//			final HttpServletRequest request,
//			@PathVariable(name = "productName", required = true) final String productName,
//			@PathVariable(name = "periodName", required = true) final String periodName
//	) throws ContentException {
//		String path = this.getPath(request);
//		logger.info(path);
//
//		HashMap<String, Object> model = this.getSelectedProductModel(productName);
//
//		this.addDownloads(model, productName, periodName);
//
//		return model;
//	}

	@GetMapping(
			value = "/content/errors/{errorCode}",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public HashMap<String, Object> errors(
			final HttpServletRequest request,
			final HttpServletResponse response,
			@PathVariable(name = "errorCode", required = true) final String errorCode
	) {
		String path = this.getPath(request);
		logger.info(path);
		HashMap<String, Object> model;
		model = this.getPageModel();
		model.put("error", errorCode);
		HashMap<String, Page> pages = this.getAllPages();
		model.put("pages", pages);
		int httpStatusCode;
		try{
			httpStatusCode = Integer.parseInt(errorCode);
		}catch (Exception e){
			httpStatusCode = 500;
		}
		response.setStatus(httpStatusCode);
		return model;
	}

	private HashMap<String, Object> buildPage(final boolean withArticles) {
		HashMap<String, Object> model = this.getPageModel();

		HashMap<String, Page> pages = this.getAllPages();
		model.put("pages", pages);

		if(withArticles) {
			Article[] articles = this.articleContent.getAllArticles();
			if (ArrayUtils.isNotEmpty(articles)) {
				List<HashMap<String, Object>> articleMaps = new ArrayList<>(articles.length);
				for (Article article : articles) {
					articleMaps.add(this.controllerUtils.articleAsMap(article));
				}
				model.put("articles", articleMaps.toArray(new HashMap[0]));
			}
		}

		return model;
	}

	protected void addDownloads(final HashMap<String, Object> model, final String productName, final String requestedPeriodName){

		String requestedRegionName = ProductServiceImpl.REGION_NAME;
		//HashMap<String, Object> model = this.getSelectedProductModel(productName);
		//this.populateSelectedProductAndPeriod(periodName, model);
		//CommercialProduct commercialProduct = (CommercialProduct) model.get("commercialProduct");
		CommercialProduct commercialProduct = this.productContent.getCommercialProduct(requestedRegionName, productName, requestedPeriodName);
		AvailableFormat[] availableFormats = this.productContent.getAvailableFormats(commercialProduct);
		if(ArrayUtils.isEmpty(availableFormats)){
			throw new ContentException("Cannot find matching downloadable product for commercialProduct", commercialProduct.toString());
		}else{
			logger.info("There are {} availableFormats and first is {}", availableFormats.length, availableFormats[0]);
		}
		// TODO: Remove baseUrl. This URL doesn't exist but is here for internal methods that expect a value in baseUrl
		String baseUrl = "checkout/binary/product/" + productName + "/period/" + requestedPeriodName + "/asset/";

		model.put("token", commercialProduct.getHash());
		HashMap<String, Page> pages = this.getAllPages();
		model.put("pages", pages);
		for(AvailableFormat availableFormat: availableFormats) {
			if(availableFormat == null){
				logger.warn("Null availableFormat in list for commercial product {}", commercialProduct);
				continue;
			}
			ProductComponent productComponent = commercialProduct.getProductComponent();
			String regionName = productComponent.getRegion().getName();
			String primaryFunctionName = productComponent.getProductType().getPrimaryFunction().getName();
			String productTypeName = productComponent.getProductType().getCatalogueName();
			String periodName = productComponent.getAccountingPeriod().getName();
			String fileFormatName = availableFormat.getFileFormat().getName();
			String fileFormatNameNoSpaces = fileFormatName.replace("%20", "");
			fileFormatNameNoSpaces = fileFormatNameNoSpaces.replace(" ", "");
			String title = regionName + " " +
					primaryFunctionName + " " +
					productTypeName + " " +
					periodName + " " +
					fileFormatName;
			model.put("title", title);
			String urlEncodedName = url.encodeQueryParam(title);
			//String exeKey = title + ".exe";
			//String zipKey = title + ".zip";
			//String exeUrl = "zips/" + exeKey;
			//String zipUrl = "zips/" + zipKey;
			// Get download URL for: <stock bucket><title>.exe and <stock bucket><title>.zip
			try {
				String exeUrl = this.productContent.getDownloadUrlForCommercialProduct(baseUrl, commercialProduct, availableFormat, ".exe");
				model.put("exe" + fileFormatNameNoSpaces + "Url", exeUrl);
			}catch (Exception e){
				logger.error("Could not find exe URL for " + title, e);
			}
			try {
				String zipUrl = this.productContent.getDownloadUrlForCommercialProduct(baseUrl, commercialProduct, availableFormat, ".zip");
				model.put("zip" + fileFormatNameNoSpaces + "Url", zipUrl);
			}catch (Exception e){
				logger.error("Could not find zip URL for " + title, e);
			}
		}
	}

	protected HashMap<String, Page> getAllPages() {
		Page[] pages = this.pageContent.getAllPages();
		HashMap<String, Page> pageMap = new HashMap<>();
		for(Page page : pages){
			String pageId = page.getName().replace("Page", "").toLowerCase(Locale.ROOT);
			pageMap.put(pageId, page);
		}
		return pageMap;
	}

	protected HashMap<String, Object> getProductPageModel(final Product product) {
		HashMap<String, Object> model = this.getPageModel();

		// Populate the product object and get the features
		String[] featureNames = product.getFeatureNames();
		Feature[] features = this.productContent.getFeatures(featureNames);
		model.put("product", product);
		model.put("features", features);
		return model;
	}

	protected HashMap<String, Object> getPageModel() {
		HashMap<String, Object> model = this.controllerUtils.buildResponseModelMapWithSiteDetails();

		model.put("products", this.productContent.getAllProductsIncludingThoseNotInCatalogue());
		model.put("availableProducts", this.productContent.getBaseProducts());

		return model;
	}

	protected HashMap<String, Object> getSelectedProductModel(final String productName) {
		Product productContentItem;
		ProductType productType = null;
		AccountingPeriod[] availablePeriods = null;
		try {
			productType = this.productContent.getProductType(productName);
			String productContentItemName = productType.getContentItemName();
			productContentItem = this.productContent.getProduct(productContentItemName);
			availablePeriods = this.productContent.getAccountingPeriods(productType);
		}catch(Exception e){
			try {
				productContentItem = this.productContent.getProduct(productName);
			}catch(Exception e2){
				productContentItem = this.productContent.getProduct(productName + "Product");
			}
		}
		HashMap<String, Object> model = this.getProductPageModel(productContentItem);
		if(productType != null) {
			model.put("selectedProduct", productType);
		}else{
			model.put("selectedProduct", productContentItem);
		}
		HashMap<String, Object> pages = new HashMap<>();
		pages.put("product", productContentItem);
		pages.put("buy", this.pageContent.getPage("BuyPage"));
		model.put("pages", pages);
		model.put("availablePeriods", availablePeriods);
		return model;
	}

	protected void populateSelectedProductAndPeriod(final String periodName, final HashMap<String, Object> model) {
		//AccountingPeriod selectedPeriod = this.decodePeriodName(periodName, selectedProduct);
		ProductType selectedProduct = (ProductType) model.get("selectedProduct");
		AccountingPeriod selectedPeriod;
		selectedPeriod = this.productContent.getAccountingPeriod(selectedProduct, periodName);
		model.put("selectedPeriod", selectedPeriod);

		CommercialProduct commercialProduct = this.getCommercialProduct(selectedProduct, selectedPeriod);
		if(commercialProduct != null) {
			String formattedPrice = commercialProduct.getBundle().buildRetailPriceFormatted();
			model.put("commercialProduct", commercialProduct);
			model.put("formattedPrice", formattedPrice);
		}
	}

	protected CommercialProduct getCommercialProduct(final ProductType selectedProduct, final AccountingPeriod selectedPeriod) {

		AccountingPeriod[] availablePeriods;
		ProductComponent[][] availableBundles;

		// Obtain the product component from the product and period to use for the bundle list and commercial
		// product
		Region region = new Region();
		region.setName(ProductServiceImpl.REGION_NAME);
		ProductComponent productComponent = this.productContent.buildProductComponent(region, selectedProduct, selectedPeriod);
		//availableBundles = this.productContent.getBundledProductComponents(productComponent);
		CommercialProduct commercialProduct;
		String[] bundleNamePeriods = null;
		//the product names probably need translating. Put a break point in the integration tests and inspect the parameters.
		commercialProduct = this.productContent.getCommercialProduct(region, productComponent, bundleNamePeriods);

		return commercialProduct;
	}

	protected String getPath(final HttpServletRequest request) {
		if(request == null){
			return "";
		}else{
			return request.getServletPath() + request.getPathInfo();
		}
	}

	public void setControllerUtils(final ControllerUtils controllerUtils) {
		this.controllerUtils = controllerUtils;
	}

	public void setArticleContent(final ArticleService articleContent) {
		this.articleContent = articleContent;
	}

	public void setCalculationUtils(final CalculationUtils calculationUtils) {
		this.calculationUtils = calculationUtils;
	}

	public void setPageContent(final PageService pageContent) {
		this.pageContent = pageContent;
	}

	public PageService getPageContent() {
		return pageContent;
	}

	public void setProductContent(final ProductService productContent) {
		this.productContent = productContent;
	}

	public ProductService getProductContent() {
		return this.productContent;
	}

	public void setSite(final Site site) {
		this.site = site;
	}

	public void setUrl(final UrlHelper url) {
		this.url = url;
	}

}
