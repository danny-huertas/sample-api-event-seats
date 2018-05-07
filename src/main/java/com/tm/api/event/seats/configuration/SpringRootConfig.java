package com.tm.api.event.seats.configuration;

import static springfox.documentation.builders.PathSelectors.regex;
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
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    @Bean
    public Docket seatsApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(SeatController.class.getPackage().getName()))
                .paths(regex(REQUEST_MAPPING + ".*")).build().apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder().title(title).description(description).version(version)
                .contact(new Contact(contactName, contactUrl, contactEmail)).build();
    }

    @Bean
    public AlwaysSampler defaultSampler() {
        return new AlwaysSampler();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns(REQUEST_MAPPING + "/**");
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SmartLocaleResolver();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        final DispatcherServlet servlet = new DispatcherServlet();
        servlet.setDispatchOptionsRequest(true);
        // Throws an exception if request url mapping not found and exception will be handled to give a 404 status code.
        servlet.setThrowExceptionIfNoHandlerFound(true);
        return servlet;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages");
        // If true, the key of the message will be displayed if the key is not
        // found, instead of throwing an exception
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        // The value 0 means always reload the messages to be developer friendly
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
