package com.personal.springlessons.endpoint;

import java.time.OffsetDateTime;

import javax.xml.namespace.QName;

import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.exception.SpringLessonsApplicationException;
import com.personal.springlessons.util.Constants;

import org.jspecify.annotations.Nullable;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

public class CustomSoapFaultResolver extends SoapFaultMappingExceptionResolver {

  @Override
  protected void customizeFault(@Nullable Object endpoint, Exception ex,
      SoapFault fault) {SoapFaultDetail detail=fault.addFaultDetail();switch(ex){case InvalidUUIDException e->this.handleInvalidUUIDException(e,detail);case SpringLessonsApplicationException e->this.handleSpringLessonsApplicationException(e,detail);default->this.handleGeneric(ex,detail);}}

  private void handleGeneric(Exception ex, SoapFaultDetail detail) {
    this.addElement(detail, Constants.S_SOAP_TAG_EXCEPTION, ex.getClass().getName());
    this.addElement(detail, Constants.S_SOAP_TAG_TIMESTAMP, OffsetDateTime.now().toString());
  }

  private void handleInvalidUUIDException(InvalidUUIDException ex, SoapFaultDetail detail) {
    this.addElement(detail, Constants.S_SOAP_TAG_ID, ex.getId());
    this.addElement(detail, Constants.S_SOAP_TAG_TIMESTAMP, OffsetDateTime.now().toString());
  }

  private void handleSpringLessonsApplicationException(SpringLessonsApplicationException ex,
      SoapFaultDetail detail) {
    this.addElement(detail, Constants.S_SOAP_TAG_EXCEPTION, ex.getClass().getName());
    this.addElement(detail, Constants.S_SOAP_TAG_TIMESTAMP, OffsetDateTime.now().toString());
  }

  private void addElement(SoapFaultDetail detail, String name, String value) {
    detail.addFaultDetailElement(new QName(Constants.S_XML_NAMESPACE_PLATFORM_FAULT, name))
        .addText(value);
  }
}
