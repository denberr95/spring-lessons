package com.personal.springlessons.config;

import java.util.Properties;
import com.personal.springlessons.endpoint.CustomSoapFaultResolver;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.util.Constants;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

@Configuration(proxyBeanMethods = false)
public class SoapWebServiceConfig {

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
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setServiceName("PlatformHistoryService");
    wsdl11Definition.setPortTypeName("PlatformHistoryPort");
    wsdl11Definition.setLocationUri("/platform");
    wsdl11Definition.setTargetNamespace(Constants.S_XML_NAMESPACE_PLATFORM_HISTORY);
    wsdl11Definition.setSchemaCollection(platformHistorySchema);
    return wsdl11Definition;
  }

  @Bean
  CommonsXsdSchemaCollection commonsXsdSchemaCollection() {
    CommonsXsdSchemaCollection commonsXsdSchemaCollection = new CommonsXsdSchemaCollection();
    commonsXsdSchemaCollection.setXsds(new ClassPathResource("schemas/PlatformHistory.xsd"));
    commonsXsdSchemaCollection.setInline(true);
    return commonsXsdSchemaCollection;
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
      CommonsXsdSchemaCollection xsdSchemaCollection) {
    PayloadValidatingInterceptor payloadValidatingInterceptor = new PayloadValidatingInterceptor();
    payloadValidatingInterceptor.setXsdSchemaCollection(xsdSchemaCollection);
    payloadValidatingInterceptor.setValidateRequest(true);
    payloadValidatingInterceptor.setValidateResponse(true);
    return payloadValidatingInterceptor;
  }

  @Bean
  CustomSoapFaultResolver customSoapFaultResolver() {
    CustomSoapFaultResolver resolver = new CustomSoapFaultResolver();
    SoapFaultDefinition faultDefinition = new SoapFaultDefinition();

    faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
    resolver.setDefaultFault(faultDefinition);

    Properties errorMappings = new Properties();
    errorMappings.setProperty(InvalidUUIDException.class.getName(),
        SoapFaultDefinition.CLIENT.toString());
    errorMappings.setProperty(SpringLessonsApplicationException.class.getName(),
        SoapFaultDefinition.SERVER.toString());

    resolver.setExceptionMappings(errorMappings);
    resolver.setOrder(1);

    return resolver;
  }
}
