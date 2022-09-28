package org.apache.fop.render.ps.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract class AbstractPSExtensionObject extends FONode {
   private final PSSetupCode setupCode = new PSSetupCode();

   public AbstractPSExtensionObject(FONode parent) {
      super(parent);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   protected void characters(char[] data, int start, int length, PropertyList pList, Locator locator) {
      String content = this.setupCode.getContent();
      if (content != null) {
         StringBuffer sb = new StringBuffer(content);
         sb.append(data, start, length);
         this.setupCode.setContent(sb.toString());
      } else {
         this.setupCode.setContent(new String(data, start, length));
      }

   }

   public String getNamespaceURI() {
      return "http://xmlgraphics.apache.org/fop/postscript";
   }

   public String getNormalNamespacePrefix() {
      return "ps";
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
      String name = attlist.getValue("name");
      if (name != null && name.length() > 0) {
         this.setupCode.setName(name);
      }

   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      String s = this.setupCode.getContent();
      if (s == null || s.length() == 0) {
         this.missingChildElementError("#PCDATA");
      }

   }

   public ExtensionAttachment getExtensionAttachment() {
      return this.setupCode;
   }
}
