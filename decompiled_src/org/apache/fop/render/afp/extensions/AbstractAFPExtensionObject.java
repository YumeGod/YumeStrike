package org.apache.fop.render.afp.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract class AbstractAFPExtensionObject extends FONode {
   protected AFPExtensionAttachment extensionAttachment;
   protected String name;

   public AbstractAFPExtensionObject(FONode parent, String name) {
      super(parent);
      this.name = name;
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getNamespaceURI() {
      return "http://xmlgraphics.apache.org/fop/extensions/afp";
   }

   public String getNormalNamespacePrefix() {
      return "afp";
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
      this.getExtensionAttachment();
      String attr = attlist.getValue("name");
      if (attr != null && attr.length() > 0) {
         this.extensionAttachment.setName(attr);
      } else {
         throw new FOPException(elementName + " must have a name attribute.");
      }
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
   }

   protected abstract ExtensionAttachment instantiateExtensionAttachment();

   public ExtensionAttachment getExtensionAttachment() {
      if (this.extensionAttachment == null) {
         this.extensionAttachment = (AFPExtensionAttachment)this.instantiateExtensionAttachment();
      }

      return this.extensionAttachment;
   }

   public String getLocalName() {
      return this.name;
   }
}
