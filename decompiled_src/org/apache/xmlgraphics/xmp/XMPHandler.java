package org.apache.xmlgraphics.xmp;

import java.util.Stack;
import org.apache.xmlgraphics.util.QName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class XMPHandler extends DefaultHandler {
   private Metadata meta;
   private StringBuffer content = new StringBuffer();
   private Stack attributesStack = new Stack();
   private Stack nestingInfoStack = new Stack();
   private Stack contextStack = new Stack();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public Metadata getMetadata() {
      return this.meta;
   }

   private boolean hasComplexContent() {
      Object obj = this.contextStack.peek();
      return !(obj instanceof QName);
   }

   private PropertyAccess getCurrentProperties() {
      Object obj = this.contextStack.peek();
      return obj instanceof PropertyAccess ? (PropertyAccess)obj : null;
   }

   private QName getCurrentPropName() {
      Object obj = this.contextStack.peek();
      return obj instanceof QName ? (QName)obj : null;
   }

   private QName popCurrentPropName() throws SAXException {
      Object obj = this.contextStack.pop();
      this.nestingInfoStack.pop();
      if (obj instanceof QName) {
         return (QName)obj;
      } else {
         throw new SAXException("Invalid XMP structure. Property name expected");
      }
   }

   private XMPComplexValue getCurrentComplexValue() {
      Object obj = this.contextStack.peek();
      return obj instanceof XMPComplexValue ? (XMPComplexValue)obj : null;
   }

   private XMPStructure getCurrentStructure() {
      Object obj = this.contextStack.peek();
      return obj instanceof XMPStructure ? (XMPStructure)obj : null;
   }

   private XMPArray getCurrentArray(boolean required) throws SAXException {
      Object obj = this.contextStack.peek();
      if (obj instanceof XMPArray) {
         return (XMPArray)obj;
      } else if (required) {
         throw new SAXException("Invalid XMP structure. Not in array");
      } else {
         return null;
      }
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      super.startElement(uri, localName, qName, attributes);
      this.content.setLength(0);
      this.attributesStack.push(new AttributesImpl(attributes));
      if ("adobe:ns:meta/".equals(uri)) {
         if (!"xmpmeta".equals(localName)) {
            throw new SAXException("Expected x:xmpmeta element, not " + qName);
         }

         if (this.meta != null) {
            throw new SAXException("Invalid XMP document. Root already received earlier.");
         }

         this.meta = new Metadata();
         this.contextStack.push(this.meta);
         this.nestingInfoStack.push("metadata");
      } else {
         QName name;
         if ("http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(uri)) {
            if ("RDF".equals(localName)) {
               if (this.meta == null) {
                  this.meta = new Metadata();
                  this.contextStack.push(this.meta);
                  this.nestingInfoStack.push("metadata");
               }
            } else if ("Description".equals(localName)) {
               String about = attributes.getValue("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "about");
               int i = 0;

               for(int c = attributes.getLength(); i < c; ++i) {
                  String ns = attributes.getURI(i);
                  if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(ns) && !"http://www.w3.org/2000/xmlns/".equals(ns) && !"".equals(ns)) {
                     String qn = attributes.getQName(i);
                     String v = attributes.getValue(i);
                     XMPProperty prop = new XMPProperty(new QName(ns, qn), v);
                     this.getCurrentProperties().setProperty(prop);
                  }
               }

               if (!this.contextStack.peek().equals(this.meta)) {
                  if (about != null) {
                     throw new SAXException("Nested rdf:Description elements may not have an about property");
                  }

                  this.startStructure();
               }
            } else {
               XMPArray array;
               if ("Seq".equals(localName)) {
                  array = new XMPArray(XMPArrayType.SEQ);
                  this.contextStack.push(array);
                  this.nestingInfoStack.push("Seq");
               } else if ("Bag".equals(localName)) {
                  array = new XMPArray(XMPArrayType.BAG);
                  this.contextStack.push(array);
                  this.nestingInfoStack.push("Bag");
               } else if ("Alt".equals(localName)) {
                  array = new XMPArray(XMPArrayType.ALT);
                  this.contextStack.push(array);
                  this.nestingInfoStack.push("Alt");
               } else if (!"li".equals(localName)) {
                  if (!"value".equals(localName)) {
                     throw new SAXException("Unexpected element in the RDF namespace: " + localName);
                  }

                  name = new QName(uri, qName);
                  this.contextStack.push(name);
                  this.nestingInfoStack.push("prop:" + name);
               }
            }
         } else {
            if (this.getCurrentPropName() != null) {
               this.startStructure();
            }

            name = new QName(uri, qName);
            this.contextStack.push(name);
            this.nestingInfoStack.push("prop:" + name);
         }
      }

   }

   private void startStructure() {
      XMPStructure struct = new XMPStructure();
      this.contextStack.push(struct);
      this.nestingInfoStack.push("struct");
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      Attributes atts = (Attributes)this.attributesStack.pop();
      if (!"adobe:ns:meta/".equals(uri)) {
         String lang;
         if ("http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(uri) && !"value".equals(localName)) {
            if ("li".equals(localName)) {
               XMPStructure struct = this.getCurrentStructure();
               if (struct != null) {
                  this.contextStack.pop();
                  this.nestingInfoStack.pop();
                  this.getCurrentArray(true).add(struct);
               } else {
                  String s = this.content.toString().trim();
                  if (s.length() > 0) {
                     lang = atts.getValue("http://www.w3.org/XML/1998/namespace", "lang");
                     if (lang != null) {
                        this.getCurrentArray(true).add(s, lang);
                     } else {
                        this.getCurrentArray(true).add(s);
                     }
                  }
               }
            } else if ("Description".equals(localName)) {
            }
         } else {
            XMPProperty prop;
            QName name;
            if (this.hasComplexContent()) {
               Object obj = this.contextStack.pop();
               this.nestingInfoStack.pop();
               name = this.popCurrentPropName();
               if (!(obj instanceof XMPComplexValue)) {
                  throw new UnsupportedOperationException("NYI");
               }

               XMPComplexValue complexValue = (XMPComplexValue)obj;
               prop = new XMPProperty(name, complexValue);
            } else {
               name = this.popCurrentPropName();
               lang = this.content.toString().trim();
               prop = new XMPProperty(name, lang);
               String lang = atts.getValue("http://www.w3.org/XML/1998/namespace", "lang");
               if (lang != null) {
                  prop.setXMLLang(lang);
               }
            }

            if (prop.getName() == null) {
               throw new IllegalStateException("No content in XMP property");
            }

            if (!$assertionsDisabled && this.getCurrentProperties() == null) {
               throw new AssertionError("no current property");
            }

            this.getCurrentProperties().setProperty(prop);
         }
      }

      this.content.setLength(0);
      super.endElement(uri, localName, qName);
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.content.append(ch, start, length);
   }

   static {
      $assertionsDisabled = !XMPHandler.class.desiredAssertionStatus();
   }
}
