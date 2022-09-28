package org.apache.fop.events.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlgraphics.util.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class EventMethodModel implements Serializable, XMLizable {
   private static final long serialVersionUID = -7548882973341444354L;
   private String methodName;
   private EventSeverity severity;
   private List params = new ArrayList();
   private String exceptionClass;

   public EventMethodModel(String methodName, EventSeverity severity) {
      this.methodName = methodName;
      this.severity = severity;
   }

   public void addParameter(Parameter param) {
      this.params.add(param);
   }

   public Parameter addParameter(Class type, String name) {
      Parameter param = new Parameter(type, name);
      this.addParameter(param);
      return param;
   }

   public void setMethodName(String name) {
      this.methodName = name;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setSeverity(EventSeverity severity) {
      this.severity = severity;
   }

   public EventSeverity getSeverity() {
      return this.severity;
   }

   public List getParameters() {
      return Collections.unmodifiableList(this.params);
   }

   public void setExceptionClass(String exceptionClass) {
      this.exceptionClass = exceptionClass;
   }

   public String getExceptionClass() {
      return this.exceptionClass;
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      atts.addAttribute("", "name", "name", "CDATA", this.getMethodName());
      atts.addAttribute("", "severity", "severity", "CDATA", this.getSeverity().getName());
      if (this.getExceptionClass() != null) {
         atts.addAttribute("", "exception", "exception", "CDATA", this.getExceptionClass());
      }

      String elName = "method";
      handler.startElement("", elName, elName, atts);
      Iterator iter = this.params.iterator();

      while(iter.hasNext()) {
         ((XMLizable)iter.next()).toSAX(handler);
      }

      handler.endElement("", elName, elName);
   }

   public static class Parameter implements Serializable, XMLizable {
      private static final long serialVersionUID = 6062500277953887099L;
      private Class type;
      private String name;

      public Parameter(Class type, String name) {
         this.type = type;
         this.name = name;
      }

      public Class getType() {
         return this.type;
      }

      public String getName() {
         return this.name;
      }

      public void toSAX(ContentHandler handler) throws SAXException {
         AttributesImpl atts = new AttributesImpl();
         atts.addAttribute("", "type", "type", "CDATA", this.getType().getName());
         atts.addAttribute("", "name", "name", "CDATA", this.getName());
         String elName = "parameter";
         handler.startElement("", elName, elName, atts);
         handler.endElement("", elName, elName);
      }
   }
}
