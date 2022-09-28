package org.apache.fop.render.ps.extensions;

import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.xmlgraphics.util.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public abstract class PSExtensionAttachment implements ExtensionAttachment, XMLizable {
   protected String content;
   public static final String CATEGORY = "apache:fop:extensions:postscript";

   public PSExtensionAttachment(String content) {
      this.content = content;
   }

   public PSExtensionAttachment() {
   }

   public String getCategory() {
      return "apache:fop:extensions:postscript";
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      String element = this.getElement();
      handler.startElement("apache:fop:extensions:postscript", element, element, atts);
      if (this.content != null && this.content.length() > 0) {
         char[] chars = this.content.toCharArray();
         handler.characters(chars, 0, chars.length);
      }

      handler.endElement("apache:fop:extensions:postscript", element, element);
   }

   public String getType() {
      String className = this.getClass().getName();
      return className.substring(className.lastIndexOf(46) + 3);
   }

   public String toString() {
      return this.getType() + ": content=" + this.content;
   }

   protected abstract String getElement();
}
