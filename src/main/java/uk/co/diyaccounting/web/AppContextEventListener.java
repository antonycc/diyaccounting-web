package uk.co.diyaccounting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AppContextEventListener {

   @Autowired
   private Environment env;

   private static final Logger logger = LoggerFactory.getLogger(DiyAccountingSpringBootApplication.class);

   @EventListener
   public void handleContextRefreshed(ContextRefreshedEvent event) {
      printActiveProperties((AbstractEnvironment) event.getApplicationContext().getEnvironment());
   }

   public void printActiveProperties() {
      this.printActiveProperties((AbstractEnvironment) env);
   }

      public void printActiveProperties(AbstractEnvironment env) {

      System.out.println("************************* ACTIVE APP PROPERTIES ******************************");

      List<MapPropertySource> propertySources = new ArrayList<>();

      env.getPropertySources().forEach(it -> {
         if (it instanceof MapPropertySource && it.getName().contains("applicationConfig")) {
            propertySources.add((MapPropertySource) it);
         }
      });

      propertySources.stream()
              .map(propertySource -> propertySource.getSource().keySet())
              .flatMap(Collection::stream)
              .distinct()
              .sorted()
              .forEach(key -> {
                 try {
                    System.out.println(key + "=" + env.getProperty(key));
                 } catch (Exception e) {
                    logger.warn("{} -> {}", key, e.getMessage());
                 }
              });
      System.out.println("******************************************************************************");
   }
}
