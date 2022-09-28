package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class FootnoteBody extends FObj {
   public FootnoteBody(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
   }

   protected void startOfNode() throws FOPException {
      this.getFOEventHandler().startFootnoteBody(this);
   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild == null) {
         this.missingChildElementError("(%block;)+");
      }

      this.getFOEventHandler().endFootnoteBody(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !this.isBlockItem(nsURI, localName)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getLocalName() {
      return "footnote-body";
   }

   public int getNameId() {
      return 25;
   }
}
