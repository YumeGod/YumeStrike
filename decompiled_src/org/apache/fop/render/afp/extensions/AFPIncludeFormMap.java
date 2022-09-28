package org.apache.fop.render.afp.extensions;

import java.net.URI;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class AFPIncludeFormMap extends AFPExtensionAttachment {
   private static final long serialVersionUID = 8548056652642588914L;
   protected static final String ATT_SRC = "src";
   protected URI src;

   public AFPIncludeFormMap() {
      super("include-form-map");
   }

   public URI getSrc() {
      return this.src;
   }

   public void setSrc(URI value) {
      this.src = value;
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      if (this.name != null && this.name.length() > 0) {
         atts.addAttribute((String)null, "name", "name", "CDATA", this.name);
      }

      if (this.src != null) {
         atts.addAttribute((String)null, "src", "src", "CDATA", this.src.toASCIIString());
      }

      handler.startElement("apache:fop:extensions:afp", this.elementName, this.elementName, atts);
      handler.endElement("apache:fop:extensions:afp", this.elementName, this.elementName);
   }

   public String toString() {
      return this.getClass().getName() + "(element-name=" + this.getElementName() + " name=" + this.getName() + " src=" + this.getSrc() + ")";
   }
}
