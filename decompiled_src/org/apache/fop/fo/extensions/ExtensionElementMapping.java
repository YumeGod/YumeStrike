package org.apache.fop.fo.extensions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.fop.fo.ElementMapping;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.UnknownXMLObj;
import org.apache.fop.fo.extensions.destination.Destination;
import org.apache.xmlgraphics.util.QName;

public class ExtensionElementMapping extends ElementMapping {
   public static final String URI = "http://xmlgraphics.apache.org/fop/extensions";
   private static final Set propertyAttributes = new HashSet();

   public ExtensionElementMapping() {
      this.namespaceURI = "http://xmlgraphics.apache.org/fop/extensions";
   }

   protected void initialize() {
      if (this.foObjs == null) {
         this.foObjs = new HashMap();
         this.foObjs.put("outline", new UnknownXMLObj.Maker("http://xmlgraphics.apache.org/fop/extensions"));
         this.foObjs.put("label", new UnknownXMLObj.Maker("http://xmlgraphics.apache.org/fop/extensions"));
         this.foObjs.put("destination", new DestinationMaker());
         this.foObjs.put("external-document", new ExternalDocumentMaker());
      }

   }

   public String getStandardPrefix() {
      return "fox";
   }

   public boolean isAttributeProperty(QName attributeName) {
      if (!"http://xmlgraphics.apache.org/fop/extensions".equals(attributeName.getNamespaceURI())) {
         throw new IllegalArgumentException("The namespace URIs don't match");
      } else {
         return propertyAttributes.contains(attributeName.getLocalName());
      }
   }

   static {
      propertyAttributes.add("block-progression-unit");
      propertyAttributes.add("widow-content-limit");
      propertyAttributes.add("orphan-content-limit");
      propertyAttributes.add("internal-destination");
      propertyAttributes.add("disable-column-balancing");
      propertyAttributes.add("alt-text");
   }

   static class ExternalDocumentMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new ExternalDocument(parent);
      }
   }

   static class DestinationMaker extends ElementMapping.Maker {
      public FONode make(FONode parent) {
         return new Destination(parent);
      }
   }
}
