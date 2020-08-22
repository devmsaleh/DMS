package com.dms;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.dms.dao.DocumentRepository;
import com.dms.servlets.ViewerServlet;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
@URLMappings(mappings = { @URLMapping(id = "default", pattern = "/", viewId = "/login.xhtml"),
		@URLMapping(id = "login", pattern = "/login", viewId = "/login.xhtml"),
		@URLMapping(id = "share", pattern = "/share", viewId = "/share.xhtml") })
public class Application extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Value("${environment}")
	private String environmentStr;

	@Autowired
	AutowireCapableBeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}

	@Bean
	public ServletContextInitializer servletContextCustomizer() {
		return new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.setInitParameter("javax.faces.STATE_SAVING_METHOD", "server");
				servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", Boolean.TRUE.toString());
				servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", Boolean.TRUE.toString());
				servletContext.setInitParameter("primefaces.FONT_AWESOME", Boolean.TRUE.toString());
				servletContext.setInitParameter("primefaces.UPLOADER", "commons");
				servletContext.setInitParameter("javax.faces.DATETIMECONVERTER_DEFAULT_TIMEZONE_IS_SYSTEM_TIMEZONE",
						Boolean.TRUE.toString());
				servletContext.setInitParameter("javax.faces.PROJECT_STAGE", environmentStr);
				servletContext.setInitParameter("primefaces.THEME", "omega");
				servletContext.setInitParameter("javax.faces.FACELETS_LIBRARIES",
						"/WEB-INF/primefaces-omega.taglib.xml");
			}
		};
	}

	@Bean
	public static CustomScopeConfigurer customScopeConfigurer() {
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		configurer.setScopes(Collections.<String, Object>singletonMap(FacesViewScope.NAME, new FacesViewScope()));
		return configurer;
	}

	@Bean
	public FilterRegistrationBean FileUploadFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new org.primefaces.webapp.filter.FileUploadFilter());
		registration.setName("PrimeFaces FileUpload Filter");
		return registration;
	}

	@Bean
	public FilterRegistrationBean hiddenHttpMethodFilterDisabled(
			@Qualifier("hiddenHttpMethodFilter") HiddenHttpMethodFilter filter) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("ar"));
		return slr;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("bundle"); // name of the resource bundle
		source.setUseCodeAsDefaultMessage(true);
		source.setDefaultEncoding("UTF-8");
		// source.setCacheSeconds(10);
		return source;
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean(DocumentRepository documentRepository) {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		ViewerServlet viewerServlet = new ViewerServlet(documentRepository);
		servletRegistrationBean.setServlet(viewerServlet);
		servletRegistrationBean.setUrlMappings(Arrays.asList("/viewer/*"));
		return servletRegistrationBean;
	}

}