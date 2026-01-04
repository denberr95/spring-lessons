package com.personal.springlessons.config;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.personal.springlessons.endpoint.CustomSoapFaultResolver;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.util.Constants;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.WsConfigurer;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

@Configuration(proxyBeanMethods = false)
public class SoapWebServiceConfig implements WsConfigurer {

  private final ObjectProvider<PayloadLoggingInterceptor> loggingInterceptorProvider;
  private final ObjectProvider<PayloadValidatingInterceptor> validatingInterceptorProvider;

  public SoapWebServiceConfig(ObjectProvider<PayloadLoggingInterceptor> loggingInterceptorProvider,
      ObjectProvider<PayloadValidatingInterceptor> validatingInterceptorProvider) {
    this.loggingInterceptorProvider = loggingInterceptorProvider;
    this.validatingInterceptorProvider = validatingInterceptorProvider;
  }

  @Bean
  ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
      ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws-soap/*");
  }

  @Bean(name = "history")
  DefaultWsdl11Definition defaultWsdl11Definition(
      CommonsXsdSchemaCollection platformHistorySchema) {
    DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
    wsdl.setServiceName("PlatformHistoryService");
    wsdl.setPortTypeName("PlatformHistoryPort");
    wsdl.setLocationUri("/platform");
    wsdl.setTargetNamespace(Constants.S_XML_NAMESPACE_PLATFORM_HISTORY);
    wsdl.setSchemaCollection(platformHistorySchema);
    return wsdl;
  }

  @Bean
  CustomSoapFaultResolver customSoapFaultResolver() {
    CustomSoapFaultResolver resolver = new CustomSoapFaultResolver();

    SoapFaultDefinition defaultFault = new SoapFaultDefinition();
    defaultFault.setFaultCode(SoapFaultDefinition.SERVER);
    defaultFault.setLocale(Locale.ENGLISH);

    Properties mappings = new Properties();
    mappings.setProperty(InvalidUUIDException.class.getName(),
        SoapFaultDefinition.CLIENT.toString());
    mappings.setProperty(SpringLessonsApplicationException.class.getName(),
        SoapFaultDefinition.SERVER.toString());

    resolver.setDefaultFault(defaultFault);
    resolver.setExceptionMappings(mappings);
    resolver.setOrder(1);

    return resolver;
  }

  @Override
  public void addInterceptors(List<EndpointInterceptor> interceptors) {
    interceptors.add(this.loggingInterceptorProvider.getObject());
    interceptors.add(this.validatingInterceptorProvider.getObject());
  }

  @Bean
  PayloadLoggingInterceptor payloadLoggingInterceptor() {
    PayloadLoggingInterceptor interceptor = new PayloadLoggingInterceptor();
    interceptor.setLogRequest(true);
    interceptor.setLogResponse(true);
    return interceptor;
  }

  @Bean
  PayloadValidatingInterceptor payloadValidatingInterceptor(
      CommonsXsdSchemaCollection platformHistorySchema) {
    PayloadValidatingInterceptor interceptor = new PayloadValidatingInterceptor();
    interceptor.setXsdSchemaCollection(platformHistorySchema);
    interceptor.setValidateRequest(true);
    interceptor.setValidateResponse(true);
    return interceptor;
  }

  @Bean
  CommonsXsdSchemaCollection platformHistorySchema() {
    CommonsXsdSchemaCollection schema = new CommonsXsdSchemaCollection();
    schema.setXsds(new ClassPathResource("schemas/PlatformHistory.xsd"));
    schema.setInline(true);
    return schema;
  }
}
