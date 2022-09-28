package org.apache.fop.fo.pagination;

import org.apache.fop.fo.FONode;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.flow.InlineLevel;
import org.xml.sax.Locator;

public class Title extends InlineLevel {
   public Title(FONode parent) {
      super(parent);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !this.isInlineItem(nsURI, localName)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getLocalName() {
      return "title";
   }

   public int getNameId() {
      return 80;
   }
}
