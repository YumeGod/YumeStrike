package org.apache.xmlgraphics.xmp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.util.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMPProperty implements XMLizable {
   private QName name;
   private Object value;
   private String xmllang;
   private Map qualifiers;

   public XMPProperty(QName name, Object value) {
      this.name = name;
      this.value = value;
   }

   public QName getName() {
      return this.name;
   }

   public String getNamespace() {
      return this.getName().getNamespaceURI();
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public Object getValue() {
      return this.value;
   }

   public void setXMLLang(String lang) {
      this.xmllang = lang;
   }

   public String getXMLLang() {
      return this.xmllang;
   }

   public boolean isArray() {
      return this.value instanceof XMPArray;
   }

   public XMPArray getArrayValue() {
      return this.isArray() ? (XMPArray)this.value : null;
   }

   public XMPArray convertSimpleValueToArray(XMPArrayType type) {
      if (this.getArrayValue() == null) {
         XMPArray array = new XMPArray(type);
         if (this.getXMLLang() != null) {
            array.add(this.getValue().toString(), this.getXMLLang());
         } else {
            array.add(this.getValue());
         }

         this.setValue(array);
         this.setXMLLang((String)null);
         return array;
      } else {
         return this.getArrayValue();
      }
   }

   public PropertyAccess getStructureValue() {
      return this.value instanceof XMPStructure ? (XMPStructure)this.value : null;
   }

   private boolean hasPropertyQualifiers() {
      return this.qualifiers == null || this.qualifiers.size() == 0;
   }

   public boolean isQualifiedProperty() {
      PropertyAccess props = this.getStructureValue();
      if (props != null) {
         XMPProperty rdfValue = props.getValueProperty();
         return rdfValue != null;
      } else {
         return this.hasPropertyQualifiers();
      }
   }

   public void simplify() {
      PropertyAccess props = this.getStructureValue();
      if (props != null) {
         XMPProperty rdfValue = props.getValueProperty();
         if (rdfValue != null) {
            if (this.hasPropertyQualifiers()) {
               throw new IllegalStateException("Illegal internal state (qualifiers present on non-simplified property)");
            }

            XMPProperty prop = new XMPProperty(this.getName(), rdfValue);
            Iterator iter = props.iterator();

            while(iter.hasNext()) {
               QName name = (QName)iter.next();
               if (!XMPConstants.RDF_VALUE.equals(name)) {
                  prop.setPropertyQualifier(name, props.getProperty(name));
               }
            }
         }
      }

   }

   private void setPropertyQualifier(QName name, XMPProperty property) {
      if (this.qualifiers == null) {
         this.qualifiers = new HashMap();
      }

      this.qualifiers.put(name, property);
   }

   private String getEffectiveQName() {
      String prefix = this.getName().getPrefix();
      if (prefix == null || "".equals(prefix)) {
         XMPSchema schema = XMPSchemaRegistry.getInstance().getSchema(this.getNamespace());
         prefix = schema.getPreferredPrefix();
      }

      return prefix + ":" + this.getName().getLocalName();
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      String qName = this.getEffectiveQName();
      handler.startElement(this.getName().getNamespaceURI(), this.getName().getLocalName(), qName, atts);
      if (this.value instanceof XMPComplexValue) {
         XMPComplexValue cv = (XMPComplexValue)this.value;
         cv.toSAX(handler);
      } else {
         char[] chars = this.value.toString().toCharArray();
         handler.characters(chars, 0, chars.length);
      }

      handler.endElement(this.getName().getNamespaceURI(), this.getName().getLocalName(), qName);
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("XMP Property ");
      sb.append(this.getName()).append(": ");
      sb.append(this.getValue());
      return sb.toString();
   }
}
