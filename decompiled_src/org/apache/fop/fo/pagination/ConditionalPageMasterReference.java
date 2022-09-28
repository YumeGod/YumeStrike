package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class ConditionalPageMasterReference extends FObj {
   private String masterReference;
   private int pagePosition;
   private int oddOrEven;
   private int blankOrNotBlank;

   public ConditionalPageMasterReference(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.masterReference = pList.get(154).getString();
      this.pagePosition = pList.get(185).getEnum();
      this.oddOrEven = pList.get(167).getEnum();
      this.blankOrNotBlank = pList.get(16).getEnum();
      if (this.masterReference == null || this.masterReference.equals("")) {
         this.missingPropertyError("master-reference");
      }

   }

   protected void startOfNode() throws FOPException {
      this.getConcreteParent().addConditionalPageMasterReference(this);
   }

   private RepeatablePageMasterAlternatives getConcreteParent() {
      return (RepeatablePageMasterAlternatives)this.parent;
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      this.invalidChildError(loc, nsURI, localName);
   }

   protected boolean isValid(boolean isOddPage, boolean isFirstPage, boolean isLastPage, boolean isBlankPage) {
      return (this.pagePosition == 8 || this.pagePosition == 50 && isFirstPage || this.pagePosition == 72 && isLastPage || this.pagePosition == 186 && isFirstPage && isLastPage || this.pagePosition == 117 && !isFirstPage && !isLastPage) && (this.oddOrEven == 8 || this.oddOrEven == 99 && isOddPage || this.oddOrEven == 43 && !isOddPage) && (this.blankOrNotBlank == 8 || this.blankOrNotBlank == 16 && isBlankPage || this.blankOrNotBlank == 98 && !isBlankPage);
   }

   public String getMasterReference() {
      return this.masterReference;
   }

   public int getPagePosition() {
      return this.pagePosition;
   }

   public String getLocalName() {
      return "conditional-page-master-reference";
   }

   public int getNameId() {
      return 12;
   }
}
