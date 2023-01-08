package uk.co.diyaccounting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

//@SpringBootApplication
//@ImportResource("classpath:/external-spring-beans.xml")
public class TestSpringBootApplication {

	private static final Logger logger = LoggerFactory.getLogger(TestSpringBootApplication.class);

	/**
	 * Console output be like:
	 *    viewResolver
	 *    webServerFactoryCustomizerBeanPostProcessor
	 *    websocketServletWebServerCustomizer
	 *    welcomePageHandlerMapping
	 *    2023-01-07T22:44:35.016+01:00  INFO 78511 --- [           main] u.c.d.web.DiyAccountingSpringBootApplication   : Application for diyaccounting-web is logging at info.
	 *    2023-01-07T22:44:35.016+01:00  WARN 78511 --- [           main] u.c.d.web.DiyAccountingSpringBootApplication   : Application for diyaccounting-web is logging at warning.
	 *    2023-01-07T22:44:35.016+01:00 ERROR 78511 --- [           main] u.c.d.web.DiyAccountingSpringBootApplication   : Application for diyaccounting-web is logging at error.
	 *    2023-01-07T23:16:30.681+01:00  INFO 81705 --- [           main] u.c.d.web.DiyAccountingSpringBootApplication   : System.getProperty("user.dir"): /Users/antony/projects/diyaccounting-web
	 *    2023-01-07T23:16:30.681+01:00  INFO 81705 --- [           main] u.c.d.web.DiyAccountingSpringBootApplication   : Size of /Users/antony/projects/diyaccounting-web/target/classes/test-content/HomePage.md is 2998
	 *    2023-01-07T23:16:30.681+01:00  INFO 81705 --- [           main] u.c.d.web.DiyAccountingSpringBootApplication   : Size of target/classes/test-content/HomePage.md is 2998
	 */
	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(TestSpringBootApplication.class, args);

		System.out.println("Application for diyaccounting-web starting.");
		logger.info("Application for diyaccounting-web is logging.");

		System.out.println("These are the system properties:");
		System.getProperties().forEach((k, v) -> System.out.println(k + ":" + v));

		System.out.println("Let's inspect the beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}

		logger.debug("Application for diyaccounting-web is logging at debug.");
		logger.info("Application for diyaccounting-web is logging at info.");
		logger.warn("Application for diyaccounting-web is logging at warning.");
		logger.error("Application for diyaccounting-web is logging at error.");

		String baseDir = System.getProperty("user.dir");
		logger.info("System.getProperty(\"user.dir\"): {}", baseDir);
		String homePageDocument = "target/classes/test-content/HomePage.md";

		Path homePagePath = Path.of(baseDir, "/", homePageDocument);
		try { logger.info("Size of {} is {}", homePagePath, Files.size(homePagePath)); }
		catch (Exception e) { logger.error("Error getting size of {}", homePagePath, e);}

		Path homePageRelativePath = Path.of(homePageDocument);
		try { logger.info("Size of {} is {}", homePageRelativePath, Files.size(homePageRelativePath)); }
		catch (Exception e) { logger.error("Error getting size of {}", homePageRelativePath, e);}
	}

}
