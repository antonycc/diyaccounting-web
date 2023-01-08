package uk.co.diyaccounting.web.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.util.Arrays;

/**
 * Start app as SpringBoot
 * 
 * @author Antony
 */
// Uncomment to run in a Spring boot configuration
// Otherwise the gb-web-local is needed on the classpath
// uk.co.diyaccounting.present.ops.Application
@SpringBootApplication
@ImportResource("classpath:/spring-boot-context.xml")
@ComponentScan(basePackages = { "uk.co.diyaccounting.web.config", "uk.co.diyaccounting.web.controller"})
public class Application {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(Application.class);

   public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

	   System.out.println("Application for DIY Accounting gb-web starting.");
		logger.info("Application for DIY Accounting gb-web is logging.");

		System.out.println("Let's inspect the beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}

}

