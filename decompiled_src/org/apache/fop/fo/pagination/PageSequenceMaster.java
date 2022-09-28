package org.apache.fop.fo.pagination;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.layoutmgr.BlockLevelEventProducer;
import org.xml.sax.Locator;

public class PageSequenceMaster extends FObj {
   private String masterName;
   private LayoutMasterSet layoutMasterSet;
   private List subSequenceSpecifiers;
   private SubSequenceSpecifier currentSubSequence;
   private int currentSubSequenceNumber = -1;

   public PageSequenceMaster(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.masterName = pList.get(153).getString();
      if (this.masterName == null || this.masterName.equals("")) {
         this.missingPropertyError("master-name");
      }

   }

   protected void startOfNode() throws FOPException {
      this.subSequenceSpecifiers = new ArrayList();
      this.layoutMasterSet = this.parent.getRoot().getLayoutMasterSet();
      this.layoutMasterSet.addPageSequenceMaster(this.masterName, this);
   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild == null) {
         this.missingChildElementError("(single-page-master-reference|repeatable-page-master-reference|repeatable-page-master-alternatives)+");
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !"single-page-master-reference".equals(localName) && !"repeatable-page-master-reference".equals(localName) && !"repeatable-page-master-alternatives".equals(localName)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   protected void addSubsequenceSpecifier(SubSequenceSpecifier pageMasterReference) {
      this.subSequenceSpecifiers.add(pageMasterReference);
   }

   private SubSequenceSpecifier getNextSubSequence() {
      ++this.currentSubSequenceNumber;
      return this.currentSubSequenceNumber >= 0 && this.currentSubSequenceNumber < this.subSequenceSpecifiers.size() ? (SubSequenceSpecifier)this.subSequenceSpecifiers.get(this.currentSubSequenceNumber) : null;
   }

   public void reset() {
      this.currentSubSequenceNumber = -1;
      this.currentSubSequence = null;
      if (this.subSequenceSpecifiers != null) {
         for(int i = 0; i < this.subSequenceSpecifiers.size(); ++i) {
            ((SubSequenceSpecifier)this.subSequenceSpecifiers.get(i)).reset();
         }
      }

   }

   public boolean goToPreviousSimplePageMaster() {
      if (this.currentSubSequence != null) {
         boolean success = this.currentSubSequence.goToPrevious();
         if (!success) {
            if (this.currentSubSequenceNumber > 0) {
               --this.currentSubSequenceNumber;
               this.currentSubSequence = (SubSequenceSpecifier)this.subSequenceSpecifiers.get(this.currentSubSequenceNumber);
            } else {
               this.currentSubSequence = null;
            }
         }
      }

      return this.currentSubSequence != null;
   }

   public boolean hasPagePositionLast() {
      return this.currentSubSequence != null && this.currentSubSequence.hasPagePositionLast();
   }

   public boolean hasPagePositionOnly() {
      return this.currentSubSequence != null && this.currentSubSequence.hasPagePositionOnly();
   }

   public SimplePageMaster getNextSimplePageMaster(boolean isOddPage, boolean isFirstPage, boolean isLastPage, boolean isBlankPage) throws PageProductionException {
      if (this.currentSubSequence == null) {
         this.currentSubSequence = this.getNextSubSequence();
         if (this.currentSubSequence == null) {
            BlockLevelEventProducer eventProducer = BlockLevelEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.missingSubsequencesInPageSequenceMaster(this, this.masterName, this.getLocator());
         }
      }

      String pageMasterName = this.currentSubSequence.getNextPageMasterName(isOddPage, isFirstPage, isLastPage, isBlankPage);

      BlockLevelEventProducer eventProducer;
      for(boolean canRecover = true; pageMasterName == null; pageMasterName = this.currentSubSequence.getNextPageMasterName(isOddPage, isFirstPage, isLastPage, isBlankPage)) {
         SubSequenceSpecifier nextSubSequence = this.getNextSubSequence();
         if (nextSubSequence == null) {
            eventProducer = BlockLevelEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.pageSequenceMasterExhausted(this, this.masterName, canRecover, this.getLocator());
            this.currentSubSequence.reset();
            canRecover = false;
         } else {
            this.currentSubSequence = nextSubSequence;
         }
      }

      SimplePageMaster pageMaster = this.layoutMasterSet.getSimplePageMaster(pageMasterName);
      if (pageMaster == null) {
         eventProducer = BlockLevelEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.noMatchingPageMaster(this, this.masterName, pageMasterName, this.getLocator());
      }

      return pageMaster;
   }

   public String getLocalName() {
      return "page-sequence-master";
   }

   public int getNameId() {
      return 54;
   }
}
