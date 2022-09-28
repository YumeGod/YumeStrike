package org.apache.fop.events.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.util.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class EventModel implements Serializable, XMLizable {
   private static final long serialVersionUID = 7468592614934605082L;
   private Map producers = new LinkedHashMap();

   public void addProducer(EventProducerModel producer) {
      this.producers.put(producer.getInterfaceName(), producer);
   }

   public Iterator getProducers() {
      return this.producers.values().iterator();
   }

   public EventProducerModel getProducer(String interfaceName) {
      return (EventProducerModel)this.producers.get(interfaceName);
   }

   public EventProducerModel getProducer(Class clazz) {
      return this.getProducer(clazz.getName());
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      String elName = "event-model";
      handler.startElement("", elName, elName, atts);
      Iterator iter = this.getProducers();

      while(iter.hasNext()) {
         ((XMLizable)iter.next()).toSAX(handler);
      }

      handler.endElement("", elName, elName);
   }

   private void writeXMLizable(XMLizable object, File outputFile) throws IOException {
      OutputStream out = new FileOutputStream(outputFile);
      OutputStream out = new BufferedOutputStream(out);
      Result res = new StreamResult(out);

      try {
         SAXTransformerFactory tFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
         TransformerHandler handler = tFactory.newTransformerHandler();
         Transformer transformer = handler.getTransformer();
         transformer.setOutputProperty("indent", "yes");
         handler.setResult(res);
         handler.startDocument();
         object.toSAX(handler);
         handler.endDocument();
      } catch (TransformerConfigurationException var13) {
         throw new IOException(var13.getMessage());
      } catch (TransformerFactoryConfigurationError var14) {
         throw new IOException(var14.getMessage());
      } catch (SAXException var15) {
         throw new IOException(var15.getMessage());
      } finally {
         IOUtils.closeQuietly((OutputStream)out);
      }

   }

   public void saveToXML(File modelFile) throws IOException {
      this.writeXMLizable(this, modelFile);
   }
}
