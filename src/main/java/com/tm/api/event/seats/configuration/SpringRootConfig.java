package com.tm.api.event.seats.configuration;

import com.tm.api.common.localization.SmartLocaleResolver;
import com.tm.api.event.seats.controllers.SeatController;
import com.tm.api.event.seats.interceptor.RequestInterceptor;
import org.h2.server.web.WebServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class is used to configure the event seats application
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@ConfigurationProperties(prefix = "root.config")
public class SpringRootConfig extends WebMvcConfigurerAdapter {
    public static final String REQUEST_MAPPING = "/tm/v1/seats";
    private String title;
    private String description;
    private String version;
    private String contactName;
    private String contactUrl;
    private String contactEmail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Sets sampler that traces each action
     *
     * @return action tracer
     */
    @Bean
    public AlwaysSampler defaultSampler() {
        return new AlwaysSampler();
    }

    /**
     * Sets a locale resolver that allows for both locale resolution via the request and locale modification via request and response.
     *
     * @return Servlet locale resolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new SmartLocaleResolver();
    }

    /**
     * Sets a servlet but with a Spring Bean friendly design. This is needed for the H2 console.
     *
     * @return Spring Bean friendly servlet
     */
    @Bean
    ServletRegistrationBean h2ServletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    /**
     * Sets the primary interface into the Springfox framework.  This is needed for swagger documentation
     *
     * @return Springfox Docket
     */
    @Bean
    public Docket seatsApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(SeatController.class.getPackage().getName()))
                .paths(PathSelectors.regex(REQUEST_MAPPING + ".*")).build().apiInfo(metaData());
    }

    /**
     * Sets Central dispatcher for HTTP request
     *
     * @return Central dispatcher for HTTP request
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        final DispatcherServlet servlet = new DispatcherServlet();
        servlet.setDispatchOptionsRequest(true);
        // Throws an exception if request url mapping not found and exception will be handled to give a 404 status code.
        servlet.setThrowExceptionIfNoHandlerFound(true);
        return servlet;
    }

    /**
     * Sets the message source used to get resource bundle messages
     *
     * @return a resource message source
     */
    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    /**
     * Adds interceptors to the registry
     *
     * @param registry helps configuring a list of mapped interceptors
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns(REQUEST_MAPPING + "/**");
    }

    /**
     * Adds resource handlers to the registry
     *
     * @param registry helps configuring a list of mapped interceptors
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Builds the meta data used by the Springfox framework.  This is needed for swagger documentation
     *
     * @return Springfox Api Info
     */
    private ApiInfo metaData() {
        return new ApiInfoBuilder().title(title).description(description).version(version)
                .contact(new Contact(contactName, contactUrl, contactEmail)).build();
    }
}
