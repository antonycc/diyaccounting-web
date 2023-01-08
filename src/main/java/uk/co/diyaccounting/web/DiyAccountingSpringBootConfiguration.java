package uk.co.diyaccounting.web;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import uk.co.diyaccounting.web.controller.CatchAllInterceptor;
import uk.co.diyaccounting.web.controller.LoggingInterceptor;
import uk.co.diyaccounting.web.controller.ReferrerInterceptor;
import uk.co.diyaccounting.web.controller.RootUrlIntercepter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Application configuration such as view mappings
 * 
 * @author Antony
 */
@Configuration("appConfigController")
//@SpringBootConfiguration
public class DiyAccountingSpringBootConfiguration implements WebMvcConfigurer {

   /**
    * Friendly URL translation
    */
   @Autowired
   @Qualifier("rootUrlIntercepter")
   private RootUrlIntercepter rootUrlIntercepter;

   /**
    * Fall through for interceptors - should be last in chain
    */
   @Autowired
   @Qualifier("catchAllInterceptor")
   private CatchAllInterceptor catchAllInterceptor;

   /**
    * Detection of referral parameters
    */
   @Autowired
   @Qualifier("referrerInterceptor")
   private ReferrerInterceptor referrerIntercepter;

   /**
    * Debugging for interceptors
    */
   @Autowired
   @Qualifier("debugInterceptor")
   private LoggingInterceptor loggingInterceptor;

   /**
    * Freemarker config
    *
    * @return a free marker config for the view resolver
    */
   @Bean
   public FreeMarkerConfig freeMarkerConfig() {
      FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
      //freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/views/");
      freeMarkerConfigurer.setTemplateLoaderPath("/");
      this.createConfiguration(freeMarkerConfigurer, "/");
      return freeMarkerConfigurer;
   }

   public void createConfiguration(FreeMarkerConfigurer freeMarkerConfigurer, String templatePath) {
      try{
         freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
         configuration.setClassForTemplateLoading(this.getClass(), templatePath);
      }catch(IOException e){
         throw new IllegalArgumentException("Could configure freemarker: [" + templatePath + "]", e);
      }catch(TemplateException e){
         throw new IllegalArgumentException("Could configure freemarker: [" + templatePath + "]", e);
      }
   }

   @Override
   public void configureViewResolvers(ViewResolverRegistry registry) {
      registry.freeMarker();
   }


   /**
    * Resolve logical view names to .jsp resources in the /WEB-INF/views directory
    * 
    * @return a view resolver that locates JSPs in the views folder
    */
   @Bean
   public ViewResolver viewResolver() {
      FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
      viewResolver.setCache(false);
      viewResolver.setPrefix("/view/");
      //viewResolver.setPrefix("/WEB-INF/views/");
      viewResolver.setSuffix(".ftl");
      viewResolver.setContentType("text/html;charset=UTF-8");
      viewResolver.setRequestContextAttribute("rc");
      viewResolver.setExposeSpringMacroHelpers(true);
      viewResolver.setExposeRequestAttributes(true);
      viewResolver.setExposeSessionAttributes(true);
      viewResolver.setAllowSessionOverride(true);
      viewResolver.setExposeSpringMacroHelpers(true);

      //viewResolver.set ToolboxConfigLocation("/WEB-INF/vel ocitytoolbox.xml");
      return viewResolver;
   }

   /**
    * Freemarker config
    *
    * @return a free marker config for the view resolver
    */
   //@Bean
   //public FreeMarkerConfig freeMarkerConfig() {
   //   FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
   //   freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/views/");
   //   this.createConfiguration(freeMarkerConfigurer, "/");
   //   return freeMarkerConfigurer;
  //}

   //public void createConfiguration(FreeMarkerConfigurer freeMarkerConfigurer, String templatePath) {
   //   try{
   //      freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
   //      configuration.setClassForTemplateLoading(this.getClass(), templatePath);
   //   }catch(IOException e){
   //      throw new IllegalArgumentException("Could configure freemarker: [" + templatePath + "]", e);
   //   }catch(TemplateException e){
   //      throw new IllegalArgumentException("Could configure freemarker: [" + templatePath + "]", e);
   //   }
   //}

   /**
    * Add intercepters
    * 
    * @return a handler with intercepters
    */
   @Bean
   public AbstractHandlerMapping handlerMapping() {
      AbstractHandlerMapping handler = new RequestMappingHandlerMapping();
      // AbstractHandlerMapping handler = new SimpleUrlHandlerMapping();
      ArrayList<Object> interceptorArray = new ArrayList<Object>();
      interceptorArray.add(this.loggingInterceptor);
      interceptorArray.add(this.referrerIntercepter);
      Object[] interceptors = interceptorArray.toArray();
      handler.setInterceptors(interceptors);
      handler.setOrder(Integer.MIN_VALUE);
	   //handler.setOrder(Integer.MAX_VALUE);
      handler.setAlwaysUseFullPath(true);
      return handler;
   }
}
