package org.apache.fop.render.afp.extensions;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class AFPIncludeFormMapElement extends AbstractAFPExtensionObject {
   private static final String ATT_SRC = "src";

   public AFPIncludeFormMapElement(FONode parent, String name) {
      super(parent, name);
   }

   private AFPIncludeFormMap getFormMapAttachment() {
      return (AFPIncludeFormMap)this.getExtensionAttachment();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      if (this.parent.getNameId() != 13) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfDeclarations");
      }

   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
      super.processNode(elementName, locator, attlist, propertyList);
      AFPIncludeFormMap formMap = this.getFormMapAttachment();
      String attr = attlist.getValue("src");
      if (attr != null && attr.length() > 0) {
         try {
            formMap.setSrc(new URI(attr));
         } catch (URISyntaxException var8) {
            this.getFOValidationEventProducer().invalidPropertyValue(this, elementName, "src", attr, (PropertyException)null, this.getLocator());
         }
      } else {
         this.missingPropertyError("src");
      }

   }

   protected ExtensionAttachment instantiateExtensionAttachment() {
      return new AFPIncludeFormMap();
   }
}
