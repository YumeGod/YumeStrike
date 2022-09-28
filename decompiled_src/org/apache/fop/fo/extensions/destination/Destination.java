package org.apache.fop.fo.extensions.destination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.pagination.Root;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class Destination extends FONode {
   private String internalDestination;
   private Root root;

   public Destination(FONode parent) {
      super(parent);
      this.root = parent.getRoot();
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      this.internalDestination = attlist.getValue("internal-destination");
      if (this.internalDestination == null || this.internalDestination.length() == 0) {
         this.missingPropertyError("internal-destination");
      }

   }

   protected void endOfNode() throws FOPException {
      this.root.addDestination(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      this.invalidChildError(loc, nsURI, localName);
   }

   public String getInternalDestination() {
      return this.internalDestination;
   }

   public String getNamespaceURI() {
      return "http://xmlgraphics.apache.org/fop/extensions";
   }

   public String getNormalNamespacePrefix() {
      return "fox";
   }

   public String getLocalName() {
      return "destination";
   }
}
