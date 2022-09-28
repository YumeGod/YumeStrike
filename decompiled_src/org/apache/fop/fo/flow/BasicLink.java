package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class BasicLink extends Inline {
   private String externalDestination;
   private String internalDestination;
   private int showDestination;
   private boolean blockOrInlineItemFound = false;

   public BasicLink(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.externalDestination = pList.get(94).getString();
      this.internalDestination = pList.get(128).getString();
      this.showDestination = pList.get(219).getEnum();
      if (this.internalDestination.length() > 0) {
         this.externalDestination = null;
      } else if (this.externalDestination.length() == 0) {
         this.getFOValidationEventProducer().missingLinkDestination(this, this.getName(), this.locator);
      }

   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startLink(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endLink();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("marker")) {
            if (this.blockOrInlineItemFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(#PCDATA|%inline;|%block;)");
            }
         } else if (!this.isBlockOrInlineItem(nsURI, localName)) {
            this.invalidChildError(loc, nsURI, localName);
         } else {
            this.blockOrInlineItemFound = true;
         }
      }

   }

   public String getInternalDestination() {
      return this.internalDestination;
   }

   public String getExternalDestination() {
      return this.externalDestination;
   }

   public boolean hasInternalDestination() {
      return this.internalDestination != null && this.internalDestination.length() > 0;
   }

   public boolean hasExternalDestination() {
      return this.externalDestination != null && this.externalDestination.length() > 0;
   }

   public int getShowDestination() {
      return this.showDestination;
   }

   public String getLocalName() {
      return "basic-link";
   }

   public int getNameId() {
      return 1;
   }
}
