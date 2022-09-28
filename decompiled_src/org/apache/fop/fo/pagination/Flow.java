package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class Flow extends FObj {
   private String flowName;
   private boolean blockItemFound = false;

   public Flow(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.flowName = pList.get(98).getString();
   }

   protected void startOfNode() throws FOPException {
      if (this.flowName == null || this.flowName.equals("")) {
         this.missingPropertyError("flow-name");
      }

      this.getFOEventHandler().startFlow(this);
   }

   protected void endOfNode() throws FOPException {
      if (!this.blockItemFound) {
         this.missingChildElementError("marker* (%block;)+");
      }

      this.getFOEventHandler().endFlow(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.blockItemFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(%block;)");
            }
         } else if (!this.isBlockItem(nsURI, localName)) {
            this.invalidChildError(loc, nsURI, localName);
         } else {
            this.blockItemFound = true;
         }
      }

   }

   public boolean generatesReferenceAreas() {
      return true;
   }

   public String getFlowName() {
      return this.flowName;
   }

   public String getLocalName() {
      return "flow";
   }

   public int getNameId() {
      return 16;
   }
}
