package org.apache.fop.render.afp.extensions;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class AFPInvokeMediumMap extends AFPExtensionAttachment {
   private static final long serialVersionUID = -7493160084509249309L;

   public AFPInvokeMediumMap() {
      super("invoke-medium-map");
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      if (this.name != null && this.name.length() > 0) {
         atts.addAttribute((String)null, "name", "name", "CDATA", this.name);
      }

      handler.startElement("apache:fop:extensions:afp", this.elementName, this.elementName, atts);
      handler.endElement("apache:fop:extensions:afp", this.elementName, this.elementName);
   }

   public String toString() {
      return "AFPInvokeMediumMap(name=" + this.getName() + ")";
   }
}
