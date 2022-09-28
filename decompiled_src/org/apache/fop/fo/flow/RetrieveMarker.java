package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class RetrieveMarker extends AbstractRetrieveMarker {
   private int retrievePosition;
   private int retrieveBoundary;

   public RetrieveMarker(FONode parent) {
      super(parent);
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      if (this.findAncestor(70) < 0) {
         this.invalidChildError(locator, this.getParent().getName(), "http://www.w3.org/1999/XSL/Format", this.getName(), "rule.retrieveMarkerDescendantOfStaticContent");
      } else {
         super.processNode(elementName, locator, attlist, pList);
      }

   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.retrievePosition = pList.get(208).getEnum();
      this.retrieveBoundary = pList.get(205).getEnum();
   }

   public int getRetrievePosition() {
      return this.retrievePosition;
   }

   public int getRetrieveBoundary() {
      return this.retrieveBoundary;
   }

   public String getLocalName() {
      return "retrieve-marker";
   }

   public int getNameId() {
      return 64;
   }
}
