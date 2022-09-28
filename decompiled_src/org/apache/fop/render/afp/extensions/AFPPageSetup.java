package org.apache.fop.render.afp.extensions;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class AFPPageSetup extends AFPExtensionAttachment {
   protected static final String ATT_VALUE = "value";
   protected String content;
   protected String value;
   private static final long serialVersionUID = -549941295384013190L;

   public AFPPageSetup(String elementName) {
      super(elementName);
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String source) {
      this.value = source;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      if (this.name != null && this.name.length() > 0) {
         atts.addAttribute((String)null, "name", "name", "CDATA", this.name);
      }

      if (this.value != null && this.value.length() > 0) {
         atts.addAttribute((String)null, "value", "value", "CDATA", this.value);
      }

      handler.startElement("apache:fop:extensions:afp", this.elementName, this.elementName, atts);
      if (this.content != null && this.content.length() > 0) {
         char[] chars = this.content.toCharArray();
         handler.characters(chars, 0, chars.length);
      }

      handler.endElement("apache:fop:extensions:afp", this.elementName, this.elementName);
   }

   public String toString() {
      return "AFPPageSetup(element-name=" + this.getElementName() + " name=" + this.getName() + " value=" + this.getValue() + ")";
   }
}
