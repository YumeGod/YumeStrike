package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FOText;
import org.apache.fop.fo.FObjMixed;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class Wrapper extends FObjMixed {
   private boolean blockOrInlineItemFound = false;

   public Wrapper(FONode parent) {
      super(parent);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if ("marker".equals(localName)) {
            if (this.blockOrInlineItemFound) {
               this.nodesOutOfOrderError(loc, "fo:marker", "(#PCDATA|%inline;|%block;)");
            }
         } else if (this.isBlockOrInlineItem(nsURI, localName)) {
            try {
               FONode.validateChildNode(this.parent, loc, nsURI, localName);
            } catch (ValidationException var5) {
               this.invalidChildError(loc, this.getName(), "http://www.w3.org/1999/XSL/Format", localName, "rule.wrapperInvalidChildForParent");
            }

            this.blockOrInlineItemFound = true;
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   protected void addChildNode(FONode child) throws FOPException {
      super.addChildNode(child);
      if (child instanceof FOText && ((FOText)child).willCreateArea()) {
         FONode ancestor;
         for(ancestor = this.parent; ancestor.getNameId() == 81; ancestor = ancestor.getParent()) {
         }

         if (!(ancestor instanceof FObjMixed)) {
            this.invalidChildError(this.getLocator(), this.getLocalName(), "http://www.w3.org/1999/XSL/Format", "#PCDATA", "rule.wrapperInvalidChildForParent");
         }
      }

   }

   public String getLocalName() {
      return "wrapper";
   }

   public int getNameId() {
      return 81;
   }
}
