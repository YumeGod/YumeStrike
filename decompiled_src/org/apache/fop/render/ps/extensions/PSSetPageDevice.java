package org.apache.fop.render.ps.extensions;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class PSSetPageDevice extends PSExtensionAttachment {
   protected static final String ELEMENT = "ps-setpagedevice";
   private static final String ATT_NAME = "name";
   protected String name;

   public PSSetPageDevice(String content) {
      super(content);
      this.name = null;
   }

   public PSSetPageDevice(String name, String content) {
      this(content);
      this.name = name;
   }

   public PSSetPageDevice() {
      this.name = null;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return "PSSetPageDevice(name=" + this.getName() + ", content='" + this.getContent() + "')";
   }

   protected String getElement() {
      return "ps-setpagedevice";
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
