package org.apache.fop.svg;

import java.io.IOException;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class FOPSAXSVGDocumentFactory extends SAXSVGDocumentFactory {
   private EntityResolver additionalResolver;

   public FOPSAXSVGDocumentFactory(String parser) {
      super(parser);
   }

   public void setAdditionalEntityResolver(EntityResolver resolver) {
      this.additionalResolver = resolver;
   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
      if (this.additionalResolver != null) {
         try {
            InputSource result = this.additionalResolver.resolveEntity(publicId, systemId);
            if (result != null) {
               return result;
            }
         } catch (IOException var4) {
            throw new SAXException(var4);
         }
      }

      return super.resolveEntity(publicId, systemId);
   }

   public Document getDocument() {
      return this.document;
   }
}
