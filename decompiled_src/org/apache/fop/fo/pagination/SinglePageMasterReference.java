package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class SinglePageMasterReference extends FObj implements SubSequenceSpecifier {
   private String masterReference;
   private static final int FIRST = 0;
   private static final int DONE = 1;
   private int state = 0;

   public SinglePageMasterReference(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.masterReference = pList.get(154).getString();
      if (this.masterReference == null || this.masterReference.equals("")) {
         this.missingPropertyError("master-reference");
      }

   }

   protected void startOfNode() throws FOPException {
      PageSequenceMaster pageSequenceMaster = (PageSequenceMaster)this.parent;
      pageSequenceMaster.addSubsequenceSpecifier(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getNextPageMasterName(boolean isOddPage, boolean isFirstPage, boolean isLastPage, boolean isBlankPage) {
      if (this.state == 0) {
         this.state = 1;
         return this.masterReference;
      } else {
         return null;
      }
   }

   public void reset() {
      this.state = 0;
   }

   public boolean goToPrevious() {
      if (this.state == 0) {
         return false;
      } else {
         this.state = 0;
         return true;
      }
   }

   public boolean hasPagePositionLast() {
      return false;
   }

   public boolean hasPagePositionOnly() {
      return false;
   }

   public String getLocalName() {
      return "single-page-master-reference";
   }

   public int getNameId() {
      return 69;
   }
}
