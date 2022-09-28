package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class MultiProperties extends FObj {
   static boolean notImplementedWarningGiven = false;
   boolean hasMultiPropertySet = false;
   boolean hasWrapper = false;

   public MultiProperties(FONode parent) {
      super(parent);
      if (!notImplementedWarningGiven) {
         this.getFOValidationEventProducer().unimplementedFeature(this, this.getName(), this.getName(), this.getLocator());
         notImplementedWarningGiven = true;
      }

   }

   protected void endOfNode() throws FOPException {
      if (!this.hasMultiPropertySet || !this.hasWrapper) {
         this.missingChildElementError("(multi-property-set+, wrapper)");
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("multi-property-set")) {
            if (this.hasWrapper) {
               this.nodesOutOfOrderError(loc, "fo:multi-property-set", "fo:wrapper");
            } else {
               this.hasMultiPropertySet = true;
            }
         } else if (localName.equals("wrapper")) {
            if (this.hasWrapper) {
               this.tooManyNodesError(loc, "fo:wrapper");
            } else {
               this.hasWrapper = true;
            }
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   public String getLocalName() {
      return "multi-properties";
   }

   public int getNameId() {
      return 46;
   }
}
