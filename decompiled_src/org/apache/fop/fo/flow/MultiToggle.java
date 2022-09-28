package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class MultiToggle extends FObj {
   static boolean notImplementedWarningGiven = false;

   public MultiToggle(FONode parent) {
      super(parent);
      if (!notImplementedWarningGiven) {
         this.getFOValidationEventProducer().unimplementedFeature(this, this.getName(), this.getName(), this.getLocator());
         notImplementedWarningGiven = true;
      }

   }

   public void bind(PropertyList pList) throws FOPException {
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !this.isBlockOrInlineItem(nsURI, localName)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getLocalName() {
      return "multi-toggle";
   }

   public int getNameId() {
      return 49;
   }
}
