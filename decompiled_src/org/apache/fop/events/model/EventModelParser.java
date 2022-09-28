package org.apache.fop.events.model;

import java.util.Stack;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.util.DefaultErrorListener;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EventModelParser {
   protected static Log log;
   private static SAXTransformerFactory tFactory;

   public static EventModel parse(Source src) throws TransformerException {
      Transformer transformer = tFactory.newTransformer();
      transformer.setErrorListener(new DefaultErrorListener(log));
      EventModel model = new EventModel();
      SAXResult res = new SAXResult(getContentHandler(model));
      transformer.transform(src, res);
      return model;
   }

   public static ContentHandler getContentHandler(EventModel model) {
      return new Handler(model);
   }

   static {
      log = LogFactory.getLog(EventModelParser.class);
      tFactory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
   }

   private static class Handler extends DefaultHandler {
      private EventModel model;
      private Stack objectStack = new Stack();

      public Handler(EventModel model) {
         this.model = model;
      }

      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         try {
            if ("event-model".equals(localName)) {
               if (this.objectStack.size() > 0) {
                  throw new SAXException("event-model must be the root element");
               }

               this.objectStack.push(this.model);
            } else if ("producer".equals(localName)) {
               EventProducerModel producer = new EventProducerModel(attributes.getValue("name"));
               EventModel parent = (EventModel)this.objectStack.peek();
               parent.addProducer(producer);
               this.objectStack.push(producer);
            } else if ("method".equals(localName)) {
               EventSeverity severity = EventSeverity.valueOf(attributes.getValue("severity"));
               String ex = attributes.getValue("exception");
               EventMethodModel method = new EventMethodModel(attributes.getValue("name"), severity);
               if (ex != null && ex.length() > 0) {
                  method.setExceptionClass(ex);
               }

               EventProducerModel parent = (EventProducerModel)this.objectStack.peek();
               parent.addMethod(method);
               this.objectStack.push(method);
            } else {
               if (!"parameter".equals(localName)) {
                  throw new SAXException("Invalid element: " + qName);
               }

               String className = attributes.getValue("type");

               Class type;
               try {
                  type = Class.forName(className);
               } catch (ClassNotFoundException var9) {
                  throw new SAXException("Could not find Class for: " + className, var9);
               }

               String name = attributes.getValue("name");
               EventMethodModel parent = (EventMethodModel)this.objectStack.peek();
               this.objectStack.push(parent.addParameter(type, name));
            }

         } catch (ClassCastException var10) {
            throw new SAXException("XML format error: " + qName, var10);
         }
      }

      public void endElement(String uri, String localName, String qName) throws SAXException {
         this.objectStack.pop();
      }
   }
}
