package org.apache.fop.events.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.xmlgraphics.util.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class EventProducerModel implements Serializable, XMLizable {
   private static final long serialVersionUID = 122267104123721902L;
   private String interfaceName;
   private Map methods = new LinkedHashMap();

   public EventProducerModel(String interfaceName) {
      this.interfaceName = interfaceName;
   }

   public String getInterfaceName() {
      return this.interfaceName;
   }

   public void setInterfaceName(String name) {
      this.interfaceName = name;
   }

   public void addMethod(EventMethodModel method) {
      this.methods.put(method.getMethodName(), method);
   }

   public EventMethodModel getMethod(String methodName) {
      return (EventMethodModel)this.methods.get(methodName);
   }

   public Iterator getMethods() {
      return this.methods.values().iterator();
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      atts.addAttribute("", "name", "name", "CDATA", this.getInterfaceName());
      String elName = "producer";
      handler.startElement("", elName, elName, atts);
      Iterator iter = this.getMethods();

      while(iter.hasNext()) {
         ((XMLizable)iter.next()).toSAX(handler);
      }

      handler.endElement("", elName, elName);
   }
}
