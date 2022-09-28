package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class Footnote extends FObj {
   private Inline footnoteCitation = null;
   private FootnoteBody footnoteBody;

   public Footnote(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
   }

   protected void startOfNode() throws FOPException {
      this.getFOEventHandler().startFootnote(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      if (this.footnoteCitation == null || this.footnoteBody == null) {
         this.missingChildElementError("(inline,footnote-body)");
      }

      this.getFOEventHandler().endFootnote(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if (localName.equals("inline")) {
            if (this.footnoteCitation != null) {
               this.tooManyNodesError(loc, "fo:inline");
            }
         } else if (localName.equals("footnote-body")) {
            if (this.footnoteCitation == null) {
               this.nodesOutOfOrderError(loc, "fo:inline", "fo:footnote-body");
            } else if (this.footnoteBody != null) {
               this.tooManyNodesError(loc, "fo:footnote-body");
            }
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   public void addChildNode(FONode child) {
      if (child.getNameId() == 35) {
         this.footnoteCitation = (Inline)child;
      } else if (child.getNameId() == 25) {
         this.footnoteBody = (FootnoteBody)child;
      }

   }

   public Inline getFootnoteCitation() {
      return this.footnoteCitation;
   }

   public FootnoteBody getFootnoteBody() {
      return this.footnoteBody;
   }

   public String getLocalName() {
      return "footnote";
   }

   public int getNameId() {
      return 24;
   }
}
