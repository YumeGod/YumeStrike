package org.apache.fop.render.afp.extensions;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class AFPPageOverlay extends AFPExtensionAttachment {
   private static final long serialVersionUID = 8548056652642588919L;
   protected static final String ATT_X = "X";
   protected static final String ATT_Y = "Y";
   private int x = 0;
   private int y = 0;

   public AFPPageOverlay() {
      super("include-page-overlay");
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
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
      return this.getClass().getName() + "(element-name=" + this.getElementName() + " name=" + this.getName() + " x=" + this.getX() + " y=" + this.getY() + ")";
   }
}
