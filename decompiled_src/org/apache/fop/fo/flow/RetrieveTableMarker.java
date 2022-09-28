package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class RetrieveTableMarker extends AbstractRetrieveMarker {
   private int retrievePositionWithinTable;
   private int retrieveBoundaryWithinTable;

   public RetrieveTableMarker(FONode parent) {
      super(parent);
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      if (this.findAncestor(78) < 0 && this.findAncestor(77) < 0) {
         this.invalidChildError(locator, this.getParent().getName(), "http://www.w3.org/1999/XSL/Format", this.getName(), "rule.retrieveTableMarkerDescendantOfHeaderOrFooter");
      } else {
         super.processNode(elementName, locator, attlist, pList);
      }

   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.retrievePositionWithinTable = pList.get(209).getEnum();
      this.retrieveBoundaryWithinTable = pList.get(206).getEnum();
   }

   public int getRetrievePositionWithinTable() {
      return this.retrievePositionWithinTable;
   }

   public int getRetrieveBoundaryWithinTable() {
      return this.retrieveBoundaryWithinTable;
   }

   public String getLocalName() {
      return "retrieve-table-marker";
   }

   public int getNameId() {
      return 65;
   }
}
