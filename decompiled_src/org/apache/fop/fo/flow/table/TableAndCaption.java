package org.apache.fop.fo.flow.table;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class TableAndCaption extends FObj {
   static boolean notImplementedWarningGiven = false;
   private boolean tableCaptionFound = false;
   private boolean tableFound = false;

   public TableAndCaption(FONode parent) {
      super(parent);
      if (!notImplementedWarningGiven) {
         this.getFOValidationEventProducer().unimplementedFeature(this, this.getName(), "fo:table-and-caption", this.getLocator());
         notImplementedWarningGiven = true;
      }

   }

   protected void endOfNode() throws FOPException {
      if (!this.tableFound) {
         this.missingChildElementError("marker* table-caption? table");
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.tableCaptionFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "fo:table-caption");
            } else if (this.tableFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "fo:table");
            }
         } else if (localName.equals("table-caption")) {
            if (this.tableCaptionFound) {
               this.tooManyNodesError(loc, "fo:table-caption");
            } else if (this.tableFound) {
               this.nodesOutOfOrderError(loc, "fo:table-caption", "fo:table");
            } else {
               this.tableCaptionFound = true;
            }
         } else if (localName.equals("table")) {
            if (this.tableFound) {
               this.tooManyNodesError(loc, "fo:table");
            } else {
               this.tableFound = true;
            }
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   public String getLocalName() {
      return "table-and-caption";
   }

   public int getNameId() {
      return 72;
   }
}
