package org.apache.fop.render.afp.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class AFPPageSetupElement extends AbstractAFPExtensionObject {
   private static final String ATT_VALUE = "value";
   private static final String ATT_SRC = "src";

   public AFPPageSetupElement(FONode parent, String name) {
      super(parent, name);
   }

   private AFPPageSetup getPageSetupAttachment() {
      return (AFPPageSetup)this.getExtensionAttachment();
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      if ("tag-logical-element".equals(this.getLocalName())) {
         if (this.parent.getNameId() != 68 && this.parent.getNameId() != 53) {
            this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfPageSequenceOrSPM");
         }
      } else if (this.parent.getNameId() != 68) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfSPM");
      }

   }

   protected void characters(char[] data, int start, int length, PropertyList pList, Locator locator) throws FOPException {
      StringBuffer sb = new StringBuffer();
      AFPPageSetup pageSetup = this.getPageSetupAttachment();
      if (pageSetup.getContent() != null) {
         sb.append(pageSetup.getContent());
      }

      sb.append(data, start, length);
      pageSetup.setContent(sb.toString());
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
      super.processNode(elementName, locator, attlist, propertyList);
      AFPPageSetup pageSetup = this.getPageSetupAttachment();
      String attr;
      if ("include-page-segment".equals(elementName)) {
         attr = attlist.getValue("src");
         if (attr != null && attr.length() > 0) {
            pageSetup.setValue(attr);
         } else {
            this.missingPropertyError("src");
         }
      } else if ("tag-logical-element".equals(elementName)) {
         attr = attlist.getValue("value");
         if (attr != null && attr.length() > 0) {
            pageSetup.setValue(attr);
         } else {
            this.missingPropertyError("value");
         }
      }

   }

   protected ExtensionAttachment instantiateExtensionAttachment() {
      return new AFPPageSetup(this.getLocalName());
   }
}
