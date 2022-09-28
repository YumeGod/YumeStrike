package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class Float extends FObj {
   static boolean notImplementedWarningGiven = false;

   public Float(FONode parent) {
      super(parent);
      if (!notImplementedWarningGiven) {
         this.getFOValidationEventProducer().unimplementedFeature(this, this.getName(), this.getName(), this.getLocator());
         notImplementedWarningGiven = true;
      }

   }

   public void bind(PropertyList pList) throws FOPException {
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !this.isBlockItem(nsURI, localName)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild == null) {
         this.missingChildElementError("(%block;)+");
      }

   }

   public String getLocalName() {
      return "float";
   }

   public int getNameId() {
      return 15;
   }
}
