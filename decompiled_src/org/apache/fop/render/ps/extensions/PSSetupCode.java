package org.apache.fop.render.ps.extensions;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class PSSetupCode extends PSExtensionAttachment {
   protected static final String ELEMENT = "ps-setup-code";
   private static final String ATT_NAME = "name";
   protected String name = null;

   public PSSetupCode() {
   }

   public PSSetupCode(String name, String content) {
      super(content);
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getCategory() {
      return "apache:fop:extensions:postscript";
   }

   public String toString() {
      return "PSSetupCode(name=" + this.getName() + ", content='" + this.getContent() + "')";
   }

   protected String getElement() {
      return "ps-setup-code";
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      if (this.name != null && this.name.length() > 0) {
         atts.addAttribute((String)null, "name", "name", "CDATA", this.name);
      }

      String element = this.getElement();
      handler.startElement("apache:fop:extensions:postscript", element, element, atts);
      if (this.content != null && this.content.length() > 0) {
         char[] chars = this.content.toCharArray();
         handler.characters(chars, 0, chars.length);
      }

      handler.endElement("apache:fop:extensions:postscript", element, element);
   }
}
