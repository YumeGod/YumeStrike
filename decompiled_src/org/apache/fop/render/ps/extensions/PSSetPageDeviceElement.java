package org.apache.fop.render.ps.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class PSSetPageDeviceElement extends AbstractPSExtensionElement {
   protected static final String ELEMENT = "ps-setpagedevice";

   protected PSSetPageDeviceElement(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      if (this.parent.getNameId() != 13 && this.parent.getNameId() != 68) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfSPMorDeclarations");
      }

   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
      String name = attlist.getValue("name");
      if (name != null && name.length() > 0) {
         ((PSSetPageDevice)this.getExtensionAttachment()).setName(name);
      }

   }

   public String getLocalName() {
      return "ps-setpagedevice";
   }

   protected ExtensionAttachment instantiateExtensionAttachment() {
      return new PSSetPageDevice();
   }
}
