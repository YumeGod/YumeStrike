package org.apache.fop.render.ps.extensions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.util.ContentHandlerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PSExtensionHandler extends DefaultHandler implements ContentHandlerFactory.ObjectSource {
   protected static Log log;
   private StringBuffer content = new StringBuffer();
   private Attributes lastAttributes;
   private PSExtensionAttachment returnedObject;
   private ContentHandlerFactory.ObjectBuiltListener listener;

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      boolean handled = false;
      if ("apache:fop:extensions:postscript".equals(uri)) {
         this.lastAttributes = attributes;
         handled = false;
         if (localName.equals("ps-setup-code") || localName.equals("ps-setpagedevice") || localName.equals("ps-comment-before") || localName.equals("ps-comment-after")) {
            handled = true;
         }
      }

      if (!handled) {
         if ("apache:fop:extensions:postscript".equals(uri)) {
            throw new SAXException("Unhandled element " + localName + " in namespace: " + uri);
         }

         log.warn("Unhandled element " + localName + " in namespace: " + uri);
      }

   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      if ("apache:fop:extensions:postscript".equals(uri)) {
         String name;
         if ("ps-setup-code".equals(localName)) {
            name = this.lastAttributes.getValue("name");
            this.returnedObject = new PSSetupCode(name, this.content.toString());
         } else if ("ps-setpagedevice".equals(localName)) {
            name = this.lastAttributes.getValue("name");
            this.returnedObject = new PSSetPageDevice(name, this.content.toString());
         } else if ("ps-comment-before".equals(localName)) {
            this.returnedObject = new PSCommentBefore(this.content.toString());
         } else if ("ps-comment-after".equals(localName)) {
            this.returnedObject = new PSCommentAfter(this.content.toString());
         }
      }

      this.content.setLength(0);
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.content.append(ch, start, length);
   }

   public void endDocument() throws SAXException {
      if (this.listener != null) {
         this.listener.notifyObjectBuilt(this.getObject());
      }

   }

   public Object getObject() {
      return this.returnedObject;
   }

   public void setObjectBuiltListener(ContentHandlerFactory.ObjectBuiltListener listener) {
      this.listener = listener;
   }

   static {
      log = LogFactory.getLog(PSExtensionHandler.class);
   }
}
