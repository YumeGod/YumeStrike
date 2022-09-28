package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.Property;
import org.xml.sax.Locator;

public class RepeatablePageMasterReference extends FObj implements SubSequenceSpecifier {
   private String masterReference;
   private Property maximumRepeats;
   private static final int INFINITE = -1;
   private int numberConsumed = 0;

   public RepeatablePageMasterReference(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.masterReference = pList.get(154).getString();
      this.maximumRepeats = pList.get(156);
      if (this.masterReference == null || this.masterReference.equals("")) {
         this.missingPropertyError("master-reference");
      }

   }

   protected void startOfNode() throws FOPException {
      PageSequenceMaster pageSequenceMaster = (PageSequenceMaster)this.parent;
      if (this.masterReference == null) {
         this.missingPropertyError("master-reference");
      } else {
         pageSequenceMaster.addSubsequenceSpecifier(this);
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      this.invalidChildError(loc, nsURI, localName);
   }

   public String getNextPageMasterName(boolean isOddPage, boolean isFirstPage, boolean isLastPage, boolean isEmptyPage) {
      if (this.getMaximumRepeats() != -1) {
         if (this.numberConsumed >= this.getMaximumRepeats()) {
            return null;
         }

         ++this.numberConsumed;
      }

      return this.masterReference;
   }

   public int getMaximumRepeats() {
      if (this.maximumRepeats.getEnum() == 89) {
         return -1;
      } else {
         int mr = this.maximumRepeats.getNumeric().getValue();
         if (mr < 0) {
            log.debug("negative maximum-repeats: " + this.maximumRepeats);
            mr = 0;
         }

         return mr;
      }
   }

   public void reset() {
      this.numberConsumed = 0;
   }

   public boolean goToPrevious() {
      if (this.numberConsumed == 0) {
         return false;
      } else {
         --this.numberConsumed;
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
      return "repeatable-page-master-reference";
   }

   public int getNameId() {
      return 63;
   }
}
