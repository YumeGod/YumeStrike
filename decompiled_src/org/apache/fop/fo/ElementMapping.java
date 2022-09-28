package org.apache.fop.fo;

import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlgraphics.util.QName;
import org.w3c.dom.DOMImplementation;

public abstract class ElementMapping {
   public static final String DEFAULT = "<default>";
   protected HashMap foObjs = null;
   protected String namespaceURI = null;

   public HashMap getTable() {
      if (this.foObjs == null) {
         this.initialize();
      }

      return this.foObjs;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public DOMImplementation getDOMImplementation() {
      return null;
   }

   public static DOMImplementation getDefaultDOMImplementation() {
      DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
      fact.setNamespaceAware(true);
      fact.setValidating(false);

      try {
         return fact.newDocumentBuilder().getDOMImplementation();
      } catch (ParserConfigurationException var2) {
         throw new RuntimeException("Cannot return default DOM implementation: " + var2.getMessage());
      }
   }

   public String getStandardPrefix() {
      return null;
   }

   public boolean isAttributeProperty(QName attributeName) {
      return false;
   }

   protected abstract void initialize();

   public static class Maker {
      public FONode make(FONode parent) {
         return null;
      }
   }
}
