package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class PageSequenceWrapper extends FObj {
   private String indexClass;
   private String indexKey;

   public PageSequenceWrapper(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.indexClass = pList.get(124).getString();
      this.indexKey = pList.get(125).getString();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !localName.equals("page-sequence") && !localName.equals("page-sequence-wrapper")) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getIndexClass() {
      return this.indexClass;
   }

   public String getIndexKey() {
      return this.indexKey;
   }

   public String getLocalName() {
      return "page-sequence-wrapper";
   }

   public int getNameId() {
      return 55;
   }
}
