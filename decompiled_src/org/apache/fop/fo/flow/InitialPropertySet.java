package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.SpaceProperty;
import org.xml.sax.Locator;

public class InitialPropertySet extends FObj {
   private SpaceProperty lineHeight;

   public InitialPropertySet(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.lineHeight = pList.get(144).getSpace();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public SpaceProperty getLineHeight() {
      return this.lineHeight;
   }

   public String getLocalName() {
      return "initial-property-set";
   }

   public int getNameId() {
      return 34;
   }
}
