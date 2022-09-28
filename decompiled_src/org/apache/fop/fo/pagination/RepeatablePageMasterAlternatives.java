package org.apache.fop.fo.pagination;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.Property;
import org.xml.sax.Locator;

public class RepeatablePageMasterAlternatives extends FObj implements SubSequenceSpecifier {
   private Property maximumRepeats;
   private static final int INFINITE = -1;
   private int numberConsumed = 0;
   private List conditionalPageMasterRefs;
   private boolean hasPagePositionLast = false;
   private boolean hasPagePositionOnly = false;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public RepeatablePageMasterAlternatives(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.maximumRepeats = pList.get(156);
   }

   protected void startOfNode() throws FOPException {
      this.conditionalPageMasterRefs = new ArrayList();
      if (!$assertionsDisabled && !this.parent.getName().equals("fo:page-sequence-master")) {
         throw new AssertionError();
      } else {
         PageSequenceMaster pageSequenceMaster = (PageSequenceMaster)this.parent;
         pageSequenceMaster.addSubsequenceSpecifier(this);
      }
   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild == null) {
         this.missingChildElementError("(conditional-page-master-reference+)");
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !localName.equals("conditional-page-master-reference")) {
         this.invalidChildError(loc, nsURI, localName);
      }

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

   public String getNextPageMasterName(boolean isOddPage, boolean isFirstPage, boolean isLastPage, boolean isBlankPage) {
      if (this.getMaximumRepeats() != -1) {
         if (this.numberConsumed >= this.getMaximumRepeats()) {
            return null;
         }

         ++this.numberConsumed;
      } else {
         ++this.numberConsumed;
      }

      for(int i = 0; i < this.conditionalPageMasterRefs.size(); ++i) {
         ConditionalPageMasterReference cpmr = (ConditionalPageMasterReference)this.conditionalPageMasterRefs.get(i);
         if (cpmr.isValid(isOddPage, isFirstPage, isLastPage, isBlankPage)) {
            return cpmr.getMasterReference();
         }
      }

      return null;
   }

   public void addConditionalPageMasterReference(ConditionalPageMasterReference cpmr) {
      this.conditionalPageMasterRefs.add(cpmr);
      if (cpmr.getPagePosition() == 72) {
         this.hasPagePositionLast = true;
      }

      if (cpmr.getPagePosition() == 186) {
         this.hasPagePositionOnly = true;
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
      return this.hasPagePositionLast;
   }

   public boolean hasPagePositionOnly() {
      return this.hasPagePositionOnly;
   }

   public String getLocalName() {
      return "repeatable-page-master-alternatives";
   }

   public int getNameId() {
      return 62;
   }

   static {
      $assertionsDisabled = !RepeatablePageMasterAlternatives.class.desiredAssertionStatus();
   }
}
