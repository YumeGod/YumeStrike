package org.apache.fop.render.ps.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.xml.sax.Locator;

public abstract class AbstractPSExtensionElement extends FONode {
   protected PSExtensionAttachment attachment;

   public AbstractPSExtensionElement(FONode parent) {
      super(parent);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   protected void characters(char[] data, int start, int length, PropertyList pList, Locator locator) {
      PSExtensionAttachment a = (PSExtensionAttachment)this.getExtensionAttachment();
      if (a.getContent() != null) {
         StringBuffer sb = new StringBuffer(a.getContent());
         sb.append(data, start, length);
         a.setContent(sb.toString());
      } else {
         a.setContent(new String(data, start, length));
      }

   }

   public String getNamespaceURI() {
      return "http://xmlgraphics.apache.org/fop/postscript";
   }

   public String getNormalNamespacePrefix() {
      return "ps";
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      String s = ((PSExtensionAttachment)this.getExtensionAttachment()).getContent();
      if (s == null || s.length() == 0) {
         this.missingChildElementError("#PCDATA");
      }

   }

   public ExtensionAttachment getExtensionAttachment() {
      if (this.attachment == null) {
         this.attachment = (PSExtensionAttachment)this.instantiateExtensionAttachment();
      }

      return this.attachment;
   }

   protected abstract ExtensionAttachment instantiateExtensionAttachment();
}
